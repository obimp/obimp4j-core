/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013—2022 Alexander Krysin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.obimp.meta;

/**
 * UserInfo
 * @author Warik777
 * @author Alexander Krysin
 */
public class UserInfo {
    private String accountName = "";
    private String nickname = "";
    private String firstName = "";
    private String lastName = "";
    private int countryCode = -1;
    private String regionState = "";
    private String city = "";
    private String zipCode = "";
    private String address = "";
    private int languageCode = -1;
    private int additionalLanguageCode = -1;
    private byte gender = -1;
    private long birthday = 0;
    private String homepage = "";
    private String about = "";
    private String interests = "";
    private String email = "";
    private String additionalEmail = "";
    private String homePhone = "";
    private String workPhone = "";
    private String cellularPhone = "";
    private String faxNumber = "";
    private boolean onlineStatus = true;
    private String company = "";
    private String divisionDepartment = "";
    private String position = "";

    /**
     * wTLD 0x0001: UTF8, Client's account name
     * @return Account name
     */
    public String getAccountName() {
        return this.accountName;
    }

    /**
     * wTLD 0x0001: UTF8, Client's account name
     * @param accountName Account name
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * wTLD 0x0002: UTF8, Nickname
     * @return Nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * wTLD 0x0002: UTF8, Nickname
     * @param nickname Nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * wTLD 0x0003: UTF8, First name
     * @return First name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * wTLD 0x0003: UTF8, First name
     * @param firstName First name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * wTLD 0x0004: UTF8, Last name
     * @return Last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * wTLD 0x0004: UTF8, Last name
     * @param lastName Last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * wTLD 0x0005: Word, Country code
     * @return Country code
     */
    public int getCountryCode() {
        return this.countryCode;
    }

    /**
     * wTLD 0x0005: Word, Country code
     * @param countryCode Country code
     */
    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * wTLD 0x0006: UTF8, Region/State
     * @return Region/State
     */
    public String getRegionState() {
        return this.regionState;
    }

    /**
     * wTLD 0x0006: UTF8, Region/State
     * @param regionState Region/State
     */
    public void setRegionState(String regionState) {
        this.regionState = regionState;
    }

    /**
     * wTLD 0x0007: UTF8, City
     * @return City
     */
    public String getCity() {
        return this.city;
    }

    /**
     * wTLD 0x0007: UTF8, City
     * @param city City
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * wTLD 0x0008: UTF8, Zip code
     * @return Zip code
     */
    public String getZipCode() {
        return this.zipCode;
    }

    /**
     * wTLD 0x0008: UTF8, Zip code
     * @param zipCode Zip code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * wTLD 0x0009: UTF8, Address
     * @return Address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * wTLD 0x0009: UTF8, Address
     * @param address Address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * wTLD 0x000A: Word, Language code
     * @return Language code
     */
    public int getLanguageCode() {
        return this.languageCode;
    }

    /**
     * wTLD 0x000A: Word, Language code
     * @param languageCode Language code
     */
    public void setLanguageCode(int languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * wTLD 0x000B: Word, Additional language code
     * @return Additional language code
     */
    public int getAdditionalLanguageCode() {
        return this.additionalLanguageCode;
    }

    /**
     * wTLD 0x000B: Word, Additional language code
     * @param additionalLanguageCode Additional language code
     */
    public void setAdditionalLanguageCode(int additionalLanguageCode) {
        this.additionalLanguageCode = additionalLanguageCode;
    }

    /**
     * wTLD 0x000C: Byte, Gender (0x00 - not specified, 0x01 - female, 0x02 - male)
     * @return Gender
     */
    public byte getGender() {
        return this.gender;
    }

    /**
     * wTLD 0x000C: Byte, Gender (0x00 - not specified, 0x01 - female, 0x02 - male)
     * @param gender Gender
     */
    public void setGender(byte gender) {
        this.gender = gender;
    }

    /**
     * wTLD 0x000D: DateTime, Birthday
     * @return Birthday
     */
    public long getBirthday() {
        return this.birthday;
    }

    /**
     * wTLD 0x000D: DateTime, Birthday
     * @param birthday Birthday
     */
    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    /**
     * wTLD 0x000E: UTF8, Homepage
     * @return Homepage
     */
    public String getHomepage() {
        return this.homepage;
    }

    /**
     * wTLD 0x000E: UTF8, Homepage
     * @param homepage Homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * wTLD 0x000F: UTF8, About
     * @return About
     */
    public String getAbout() {
        return this.about;
    }

    /**
     * wTLD 0x000F: UTF8, About
     * @param about About
     */
    public void setAbout(String about) {
        this.about = about;
    }

    /**
     * wTLD 0x0010: UTF8, Interests
     * @return Interests
     */
    public String getInterests() {
        return this.interests;
    }

    /**
     * wTLD 0x0010: UTF8, Interests
     * @param interests Interests
     */
    public void setInterests(String interests) {
        this.interests = interests;
    }

    /**
     * wTLD 0x0011: UTF8, Email
     * @return Email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * wTLD 0x0011: UTF8, Email
     * @param email Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * wTLD 0x0012: UTF8, Additional email
     * @return Additional email
     */
    public String getAdditionalEmail() {
        return this.additionalEmail;
    }

    /**
     * wTLD 0x0012: UTF8, Additional email
     * @param additionalEmail Addition email
     */
    public void setAdditionalEmail(String additionalEmail) {
        this.additionalEmail = additionalEmail;
    }

    /**
     * wTLD 0x0013: UTF8, Home phone
     * @return Home phone
     */
    public String getHomePhone() {
        return this.homePhone;
    }

    /**
     * wTLD 0x0013: UTF8, Home phone
     * @param homePhone Home phone
     */
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    /**
     * wTLD 0x0014: UTF8, Work phone
     * @return Work phone
     */
    public String getWorkPhone() {
        return this.workPhone;
    }

    /**
     * wTLD 0x0014: UTF8, Work phone
     * @param workPhone Work phone
     */
    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    /**
     * wTLD 0x0015: UTF8, Cellular phone
     * @return Cellular phone
     */
    public String getCellularPhone() {
        return this.cellularPhone;
    }

    /**
     * wTLD 0x0015: UTF8, Cellular phone
     * @param cellularPhone Cellular phone
     */
    public void setCellularPhone(String cellularPhone) {
        this.cellularPhone = cellularPhone;
    }

    /**
     * wTLD 0x0016: UTF8, Fax number
     * @return Fax number
     */
    public String getFaxNumber() {
        return this.faxNumber;
    }

    /**
     * wTLD 0x0016: UTF8, Fax number
     * @param faxNumber Fax number
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * wTLD 0x0017: Bool, if True then online status will not be shown in users directory
     * @return Show online status?
     */
    public boolean getOnlineStatus() {
        return this.onlineStatus;
    }

    /**
     * wTLD 0x0017: Bool, if True then online status will not be shown in users directory
     * @param onlineStatus Show online status?
     */
    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /**
     * wTLD 0x0018: UTF8, Company
     * @return Company
     */
    public String getCompany() {
        return this.company;
    }

    /**
     * wTLD 0x0018: UTF8, Company
     * @param company Company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * wTLD 0x0019: UTF8, Division or department
     * @return Division or department
     */
    public String getDivisionDepartment() {
        return this.divisionDepartment;
    }

    /**
     * wTLD 0x0019: UTF8, Division or department
     * @param divisionDepartment Division or department
     */
    public void setDivisionDepartment(String divisionDepartment) {
        this.divisionDepartment = divisionDepartment;
    }

    /**
     * wTLD 0x001A: UTF8, Position
     * @return Position
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * wTLD 0x001A: UTF8, Position
     * @param position Position
     */
    public void setPosition(String position) {
        this.position = position;
    }
}
