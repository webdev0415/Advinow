package com.advinow.mica.model.mita;

public class MitaValueObject {

	CollectorValueObject collector;

	ReviewerValueObject reviewer;

	public CollectorValueObject getCollector() {
		return collector;
	}

	public void setCollector(CollectorValueObject collector) {
		this.collector = collector;
	}

	public ReviewerValueObject getReviewer() {
		return reviewer;
	}

	public void setReviewer(ReviewerValueObject reviewer) {
		this.reviewer = reviewer;
	}
}