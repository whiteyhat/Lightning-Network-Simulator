/*
 * Copyright (c) 2019 Carlos Roldan Torregrosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.carlos.lnsim.lnsim;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String secret;
    private String paymentRequest;
    private Double tokens;
    private Date createdAt;
    private Date expiredAt;
    private Node to;

    public Transaction(Node to, Double tokens) {
        this.secret = UUID.randomUUID().toString();
        this.paymentRequest = String.valueOf(secret.hashCode());
        this.tokens = tokens;
        Calendar expiration = Calendar.getInstance(); // creates calendar
        expiration.setTime(new Date()); // sets calendar time/date
        expiration.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

        Calendar creation = Calendar.getInstance(); // creates calendar
        creation.setTime(new Date()); // sets calendar time/date
        this.createdAt = creation.getTime();
        this.expiredAt = expiration.getTime();
        this.to = to;
    }

    public Node getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                ", To='" + to.getId() + '\'' +", secret='" + secret + '\'' +
                ", paymentRequest='" + paymentRequest + '\'' +
                ", tokens=" + tokens +
                ", createdAt=" + createdAt +
                ", expiredAt=" + expiredAt +
                '}';
    }
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(String paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public Double getTokens() {
        return tokens;
    }

    public void setTokens(Double tokens) {
        this.tokens = tokens;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }
}


