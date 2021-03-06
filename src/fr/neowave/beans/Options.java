package fr.neowave.beans;


import java.io.Serializable;

/**
 * Représentation de la table Options
 */
public class Options implements Serializable{

    private Boolean onlyNeowave;
    private Boolean adminReplaceUsersTokens;
    private Boolean userCreateAccount;
    private Boolean usersRegisterTheirOwnFirstToken;
    private Boolean usersAddNewTokens;
    private Boolean usersRemoveLastToken;
    private Boolean usersSeeDetails;
    private Long sessionInactiveExpirationTime;
    private Long requestValidityTime;
    private Long delayToPutToken;

    public Boolean getOnlyNeowave() {
        return onlyNeowave;
    }

    public void setOnlyNeowave(Boolean onlyNeowave) {
        this.onlyNeowave = onlyNeowave;
    }

    public Boolean getAdminReplaceUsersTokens() {
        return adminReplaceUsersTokens;
    }

    public void setAdminReplaceUsersTokens(Boolean adminReplaceUsersTokens) {
        this.adminReplaceUsersTokens = adminReplaceUsersTokens;
    }

    public Boolean getUserCreateAccount() {
        return userCreateAccount;
    }

    public void setUserCreateAccount(Boolean userCreateAccount) {
        this.userCreateAccount = userCreateAccount;
    }

    public Boolean getUsersRegisterTheirOwnFirstToken() {
        return usersRegisterTheirOwnFirstToken;
    }

    public void setUsersRegisterTheirOwnFirstToken(Boolean usersRegisterTheirOwnFirstToken) {
        this.usersRegisterTheirOwnFirstToken = usersRegisterTheirOwnFirstToken;
    }

    public Boolean getUsersAddNewTokens() {
        return usersAddNewTokens;
    }

    public void setUsersAddNewTokens(Boolean usersAddNewTokens) {
        this.usersAddNewTokens = usersAddNewTokens;
    }

    public Boolean getUsersRemoveLastToken() {
        return usersRemoveLastToken;
    }

    public void setUsersRemoveLastToken(Boolean usersRemoveLastToken) {
        this.usersRemoveLastToken = usersRemoveLastToken;
    }

    public Boolean getUsersSeeDetails() {
        return usersSeeDetails;
    }

    public void setUsersSeeDetails(Boolean usersSeeDetails) {
        this.usersSeeDetails = usersSeeDetails;
    }


    public Long getSessionInactiveExpirationTime() {
        return sessionInactiveExpirationTime;
    }

    public void setSessionInactiveExpirationTime(Long sessionInactiveExpirationTime) {
        this.sessionInactiveExpirationTime = sessionInactiveExpirationTime;
    }

    public Long getRequestValidityTime() {
        return requestValidityTime;
    }

    public void setRequestValidityTime(Long requestValidityTime) {
        this.requestValidityTime = requestValidityTime;
    }

    public Long getDelayToPutToken() {
        return delayToPutToken;
    }

    public void setDelayToPutToken(Long delayToPutToken) {
        this.delayToPutToken = delayToPutToken;
    }
}
