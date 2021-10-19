/*
 * Developed by Sokolov Alexey aka scream3r for 7pikes inc., 2014.
 *
 * scream3r.org@gmail.com
 * http://scream3r.org
 *
 * http://7pikes.com
 *
 * © Sokolov Alexey, 7pikes inc., 2014.
 */
package ru.unpos.hw.service.acquiring.sber.types;

/**
 *
 * @author scream3r
 */
public class TransactionInfo {

    private boolean intermediate;
    private String cardNumber;
    private String cardExpDate;
    private String authCode;
    private String operationNumber;
    private String cardType;
    private String cardSB;
    private String terminalNumber;
    private String timeStamp;
    private String RRN;
    private String SHA1;

    public TransactionInfo(boolean intermediate,String cardNumber, String cardExpDate, String authCode, String operationNumber, String cardType, String cardSB, String terminalNumber, String timeStamp, String RRN, String SHA1) {
        this.intermediate = intermediate;
        this.cardNumber = cardNumber;
        this.cardExpDate = cardExpDate;
        this.authCode = authCode;
        this.operationNumber = operationNumber;
        this.cardType = cardType;
        this.cardSB = cardSB;
        this.terminalNumber = terminalNumber;
        this.timeStamp = timeStamp;
        this.RRN = RRN;
        this.SHA1 = SHA1;
    }

    public boolean isIntermediate() {
        return intermediate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpDate() {
        return cardExpDate;
    }

    public void setCardExpDate(String cardExpDate) {
        this.cardExpDate = cardExpDate;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardSB() {
        return cardSB;
    }

    public void setCardSB(String cardSB) {
        this.cardSB = cardSB;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getSHA1() {
        return SHA1;
    }

    public void setSHA1(String SHA1) {
        this.SHA1 = SHA1;
    }
}
