package com.food.order.system.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class IssueDataModel {

    private String reason;
    private String action;

    /**
     * Instantiates a new issue data model.
     */
    public IssueDataModel() {
        // default constructor
    }

    /**
     * Instantiates a new issue data model.
     *
     * @param issueDataModel the issue data model
     */
    public IssueDataModel(IssueDataModel issueDataModel) {
        this.reason = issueDataModel.reason;
        this.action = issueDataModel.action;
    }


    /**
     * Gets the reason.
     *
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason.
     *
     * @param reason the new reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Instantiates a new issue data model.
     *
     * @param resource the resource
     * @param errorcode the errorcode
     * @param reason the reason
     * @param action the action
     */
    public IssueDataModel(String resource, String errorcode, String reason, String action) {
		this.reason = reason;
		this.action = action;
	}

	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "IssueDataModel [ reason=" + reason + ", action="
                                        + action + "]";
    }
}