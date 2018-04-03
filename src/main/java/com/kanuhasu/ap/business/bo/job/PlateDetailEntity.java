package com.kanuhasu.ap.business.bo.job;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kanuhasu.ap.business.pojo.Plate;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table(name = "PLATE_HOLDER")
public class PlateDetailEntity extends LastUpdateEntity {
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "PLATE_DETAIL", joinColumns = @JoinColumn(name = "jobID"), inverseJoinColumns = @JoinColumn(name = "plateID"))
	private Set<PlateEntity> plates = new HashSet<>();

	/** ------------| business |------------ **/

	public PlateEntity processPlate(Plate pojo) {
		PlateEntity currentPlate = null;
		if (CommonUtil.isIdDefined(pojo.getId())) {
			for (PlateEntity entity : this.getPlates()) {
				if (entity.getId() == pojo.getId()) {
					currentPlate = entity;
					break;
				}
			}
		} else {
			currentPlate = new PlateEntity();
			this.getPlates().add(currentPlate);
		}
		currentPlate.override(pojo);
		return currentPlate;
	}

	/** ------------| setter-getter |------------ **/

	public Set<PlateEntity> getPlates() {
		return plates;
	}

	public void setPlates(Set<PlateEntity> plates) {
		this.plates = plates;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
