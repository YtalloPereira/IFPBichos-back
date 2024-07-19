package ifpb.edu.br.pj.ifpbichos.business.service;

import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import ifpb.edu.br.pj.ifpbichos.model.entity.Campaign;
import ifpb.edu.br.pj.ifpbichos.model.entity.Donation;
import ifpb.edu.br.pj.ifpbichos.model.entity.User;
import ifpb.edu.br.pj.ifpbichos.model.enums.DonationPaymentStatus;
import ifpb.edu.br.pj.ifpbichos.model.repository.CampaignRepository;
import ifpb.edu.br.pj.ifpbichos.model.repository.DonationRepository;
import ifpb.edu.br.pj.ifpbichos.model.repository.DonatorRepository;
import ifpb.edu.br.pj.ifpbichos.model.repository.UserRepository;
import ifpb.edu.br.pj.ifpbichos.presentation.exception.PaymentProcessingException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class MercadoPagoService {

    private final DonatorRepository donatorRepository;
    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(MercadoPagoService.class.getName());

    @Autowired
    public MercadoPagoService(DonatorRepository donatorRepository, DonationRepository donationRepository,
                              CampaignRepository campaignRepository, UserRepository userRepository) {
        this.donatorRepository = donatorRepository;
        this.donationRepository = donationRepository;
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    public Preference createPayment(String title, String description, BigDecimal transactionAmount, Integer installments, Long campaignId, String userLogin, String url, Boolean isDirected) throws PaymentProcessingException {
        try {
            PreferenceClient client = new PreferenceClient();

            User user = (User) userRepository.findByLogin(userLogin);

            PreferenceRequest preferenceRequest = buildPreferenceRequest(title, description, transactionAmount, installments, user, url);

            Preference preference = client.create(preferenceRequest);

            saveDonation(preference.getId(), transactionAmount, campaignId, userLogin, isDirected);

            return preference;
        } catch (MPException | MPApiException e) {
            throw new PaymentProcessingException("Failed to create payment", e);
        }
    }

    private PreferenceRequest buildPreferenceRequest(String title, String description, BigDecimal transactionAmount, Integer installments, User user, String url) {
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(UUID.randomUUID().toString())
                .title(title)
                .description(description)
                .categoryId("currency_id")
                .quantity(1)
                .currencyId("BRL")
                .unitPrice(transactionAmount)
                .build();

        List<PreferenceItemRequest> items = Collections.singletonList(itemRequest);

        return PreferenceRequest.builder()
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success(url)
                        .failure("http://localhost:5173/campanhas")
                        .pending("http://localhost:5173/pending")
                        .build())
                .differentialPricing(PreferenceDifferentialPricingRequest.builder()
                        .id(1L)
                        .build())
                .expires(false)
                .items(items)
                .marketplaceFee(BigDecimal.ZERO)
                .autoReturn("all")
                .payer(buildPayerRequest(user))
                .binaryMode(true)
                .externalReference("1643827245")
                .marketplace("marketplace")
                .notificationUrl("http://notificationurl.com")
                .operationType("regular_payment")
                .build();
    }

    private PreferencePayerRequest buildPayerRequest(User user) {
        return PreferencePayerRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(PhoneRequest.builder()
                        .areaCode(user.getPhoneNumber().substring(0, 2))
                        .number(user.getPhoneNumber().substring(2))
                        .build())
                .identification(IdentificationRequest.builder()
                        .type("CPF")
                        .number(user.getCPF())
                        .build())
                .build();
    }
    public Donation updatePayment(String preferenceId, String paymentId, String status, String paymentType) {

        Donation donation = donationRepository.findByPreferenceId(preferenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        Campaign campaign = campaignRepository.findById(donation.getCampaign().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));

        if(donation.getPaymentId() == null){
            campaign.setBalance(campaign.getBalance().add(donation.getDonationValue()));
            campaignRepository.save(campaign);

            donation.setPaymentId(paymentId);
            donation.setStatus(DonationPaymentStatus.APPROVED);
            donation.setPaymentType(paymentType);
        }


        return donationRepository.save(donation);
    }

    public void saveDonation(String preferenceId, BigDecimal transactionAmount,Long campaignId, String userLogin, Boolean isDirected) {

        User user = (User) userRepository.findByLogin(userLogin);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Campaign campaign = null;
        if (isDirected) {
            campaign = campaignRepository.findById(campaignId)
                    .orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
        }

        Donation donation = new Donation();

        donation.setDate(LocalDateTime.now());
        donation.setCampaign(campaign);
        donation.setDirected(isDirected);
        donation.setPreferenceId(preferenceId);
        donation.setDonationValue(transactionAmount);
        donation.setStatus(DonationPaymentStatus.PENDING);
        donation.setDonator(user);

        user.getDonations().add(donation);

        donationRepository.save(donation);
    }

}