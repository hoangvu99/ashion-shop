package com.example.model.order;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	private int status;
	
	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
	private List<OrderItems> orderItems;
	private String createdAt;
	private String updatedAt;
	private long orderTotal;
	
	private int paymentMethod;
	private int isPaid;
	
	private String note;
	
	
	private String deliveryAddress;
	private long phoneNumber;
	
	public void calTotal() {
		long total = 0;
		for (int i = 0; i < this.orderItems.size(); i++) {
			total +=  this.orderItems.get(i).getTotal();
		}
		
		this.orderTotal= total;
	}
	
}