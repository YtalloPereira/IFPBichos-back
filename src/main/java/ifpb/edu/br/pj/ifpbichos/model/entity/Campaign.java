package ifpb.edu.br.pj.ifpbichos.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import ifpb.edu.br.pj.ifpbichos.model.enums.Animal;
import jakarta.persistence.*;


@Table(name = "CAMPAING")
@Entity
public class Campaign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "START_DATE", nullable = false)
	private LocalDateTime start;
	@Column(name = "END_DATE", nullable = false)
	private LocalDateTime end;
	@Column(name = "CAMPAIGN_TITLE", nullable = false, unique = true)
	private String title;
	@Column(name = "CAMPAIGN_DESCRIPTION")
	private String description;
	//true = ativa. false = encerrada

	@Column(name = "CAMPAIGN_ANIMAL")
	private Animal animal;


	@Column(name = "CAMPAIGN_STATUS", nullable = false)
	private boolean campaingStatus;

	@Lob
	@Column(name = "CAMPAIGN_PET_IMAGE")
	private byte[] image;
	@Column(name = "COLLECTION_GOAL", nullable = false)
	private float collectionGoal;
	@Column(name = "COLLECTION_PERCENTAGE", nullable = false)
	private float collectionPercentage;
	@Column(name = "BALANCE", nullable = false)
	private float balance;
	@Column(name = "UNDIRECTED_BALANCE", nullable = false)
	private float undirectedBalance;
	
	public Campaign() {
		
	}
	
	public Campaign(LocalDateTime start, LocalDateTime end, String title, String description, byte[] image,float collectionPercentage,  float balance,
	float undirectedBalance,Animal animal) {
		this.start = start;
		this.end = end;
		this.title = title;
		this.description = description;
		this.image = image;
		this.collectionPercentage = collectionPercentage;
		this.balance = 0;
		this.undirectedBalance = 0;
		this.animal=animal;
	}

	public Campaign(Integer id, LocalDateTime start, LocalDateTime end, String title, String description, boolean campaingStatus, byte[] image, float collectionGoal, float collectionPercentage, float balance, float undirectedBalance, Animal animal) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.title = title;
		this.description = description;
		this.campaingStatus = campaingStatus;
		this.image = image;
		this.collectionGoal = collectionGoal;
		this.collectionPercentage = collectionPercentage;
		this.balance = balance;
		this.undirectedBalance = undirectedBalance;
		this.animal=animal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCampaingStatus() {
		return campaingStatus;
	}

	public void setCampaingStatus(boolean campaingStatus) {
		this.campaingStatus = campaingStatus;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public float getCollectionGoal() {
		return collectionGoal;
	}

	public void setCollectionGoal(float collectionGoal) {
		this.collectionGoal = collectionGoal;
	}

	public float getCollectionPercentage() {
		return collectionPercentage;
	}

	public void setCollectionPercentage(float collectionPercentage) {
		this.collectionPercentage = collectionPercentage;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public float getUndirectedBalance() {
		return undirectedBalance;
	}

	public Animal getAnimal(){
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public void setUndirectedBalance(float undirectedBalance) {
		this.undirectedBalance = undirectedBalance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public int hashCode() {
		return Objects.hash(balance, campaingStatus, collectionGoal, collectionPercentage, description, end, id, image,
				start, title, undirectedBalance);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Campaign other = (Campaign) obj;
		return Float.floatToIntBits(balance) == Float.floatToIntBits(other.balance)
				&& campaingStatus == other.campaingStatus
				&& Float.floatToIntBits(collectionGoal) == Float.floatToIntBits(other.collectionGoal)
				&& Float.floatToIntBits(collectionPercentage) == Float.floatToIntBits(other.collectionPercentage)
				&& Objects.equals(description, other.description) && Objects.equals(end, other.end)
				&& Objects.equals(id, other.id) && Objects.equals(image, other.image)
				&& Objects.equals(start, other.start) && Objects.equals(title, other.title)
				&& Float.floatToIntBits(undirectedBalance) == Float.floatToIntBits(other.undirectedBalance);
	}

	@Override
	public String toString() {
		return "Campaign [id=" + id + ", start=" + start + ", end=" + end + ", title=" + title + ", description="
				+ description + ", campaingStatus=" + campaingStatus + ", image=" + image + ", collectionGoal="
				+ collectionGoal + ", collectionPercentage=" + collectionPercentage + ", balance=" + balance
				+ ", undirectedBalance=" + undirectedBalance + "]";
	}
	
	
	
}
