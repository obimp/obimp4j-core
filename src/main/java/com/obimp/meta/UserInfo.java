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

package com.obimp.meta;
 
/**
 * UserInfo
 * @author Warik777
 */
public class UserInfo {
private String account_name = "";
private String nick_name = "";
private String first_name = "";
private String last_name = "";
private int country_code = -1;
private String region_state = "";
private String city = "";
private String zip_code = "";
private String address = "";
private int language_code = -1;
private int additional_language_code = -1;
private byte gender = -1;
private long birthday = 0;
private String homepage = "";
private String about = "";
private String interests = "";
private String email = "";
private String additional_email = "";
private String home_phone = "";
private String work_phone = "";
private String cellular_phone = "";
private String fax_number = "";
private boolean online_status = true;
private String company = "";
private String division_department = "";
private String position = "";
 
/**
 * wTLD 0x0001: UTF8, client's account name
 * @return
 */
public String getAccountName(){
    return this.account_name;
}
 
/**
 * wTLD 0x0002: UTF8, nick name
 * @return
 */
public String getNickName(){
    return this.nick_name;
}
 
/**
 * wTLD 0x0003: UTF8, first name
 * @return
 */
public String getFirstName(){
    return this.first_name;
}
 
/**
 * wTLD 0x0004: UTF8, last name
 * @return
 */
public String getLastName(){
    return this.last_name;
}
 
/**
 * wTLD 0x0005: Word, country code (can be found at the end of this document)
 * @return
 */
public int getCountryCode(){
    return this.country_code;
}
 
/**
 * wTLD 0x0006: UTF8, region/state
 * @return
 */
public String getRegionState(){
    return this.region_state;
}
 
/**
 * wTLD 0x0007: UTF8, city
 * @return
 */
public String getCity(){
    return this.city;
}
 
/**
 * wTLD 0x0008: UTF8, zip code
 * @return
 */
public String getZipCode(){
    return this.zip_code;
}
 
/**
 * wTLD 0x0009: UTF8, address
 * @return
 */
public String getAddress(){
    return this.address;
}
 
/**
 * wTLD 0x000A: Word, language code (can be found at the end of this document)
 * @return
 */
public int getLanguageCode(){
    return this.language_code;
}
 
/**
 * wTLD 0x000B: Word, additional language code (can be found at the end of this document)
 * @return
 */
public int getAditionalLanguageCode(){
    return this.additional_language_code;
}
 
/**
 * wTLD 0x000C: Byte, gender (0x00 - not specified, 0x01 - female, 0x02 - male)
 * @return
 */
public byte getGender(){
    return this.gender;
}
 
/**
 * wTLD 0x000D: DateTime, birthday
 * @return
 */
public long getBirthday(){
    return this.birthday;
}
 
/**
 * wTLD 0x000E: UTF8, homepage
 * @return
 */
public String getHomepage(){
    return this.homepage;
}
 
/**
 * wTLD 0x000F: UTF8, about
 * @return
 */
public String getAbout(){
    return this.about;
}
 
/**
 * wTLD 0x0010: UTF8, interests
 * @return
 */
public String getInterests(){
    return this.interests;
}
 
/**
 * wTLD 0x0011: UTF8, email
 * @return
 */
public String getEmail(){
    return this.email;
}
 
/**
 * wTLD 0x0012: UTF8, additional email
 * @return
 */
public String getAdditionalEmail(){
    return this.additional_email;
}
 
/**
 * wTLD 0x0013: UTF8, home phone
 * @return
 */
public String getHomePhone(){
    return this.home_phone;
}
 
/**
 * wTLD 0x0014: UTF8, work phone
 * @return
 */
public String getWorkPhone(){
    return this.work_phone;
}
 
/**
 * wTLD 0x0015: UTF8, cellular phone
 * @return
 */
public String getCellularPhone(){
    return this.cellular_phone;
}
 
/**
 * wTLD 0x0016: UTF8, fax number
 * @return
 */
public String getFaxNumber(){
    return this.fax_number;
}
 
/**
 * wTLD 0x0017: Bool, if True then online status will not be shown in users directory
 * @return
 */
public boolean getOnlineStatus(){
    return this.online_status;
}
 
/**
 * wTLD 0x0018: UTF8, company
 * @return
 */
public String getCompany(){
    return this.company;
}
 
/**
 * wTLD 0x0019: UTF8, division/department
 * @return
 */
public String getDivisionDepartment(){
    return this.division_department;
}
 
/**
 * wTLD 0x001A: UTF8, position
 * @return
 */
public String getPosition(){
    return this.position;
}
 
/**
 * wTLD 0x0001: UTF8, client's account name
 * @param account_name_ логин
 */
public void setAccountName(String account_name_){
    this.account_name = account_name_;
}
 
/**
 * wTLD 0x0002: UTF8, nick name
 * @param nick_name_ ник
 */
public void setNickName(String nick_name_){
    this.nick_name = nick_name_;
}
 
/**
 * wTLD 0x0003: UTF8, first name
 * @param first_name_ имя
 */
public void setFirstName(String first_name_){
    this.first_name = first_name_;
}
 
/**
 * wTLD 0x0004: UTF8, last name
 * @param last_name_ фамилия
 */
public void setLastName(String last_name_){
    this.last_name = last_name_;
}
 
/**
 * wTLD 0x0005: Word, country code (can be found at the end of this document)
 * @param country_code_ страна
 */
public void setCountryCode(int country_code_){
    this.country_code = country_code_;
}
 
/**
 * wTLD 0x0006: UTF8, region/state
 * @param region_state_ регион/область/штат
 */
public void setRegionState(String region_state_){
    this.region_state = region_state_;
}
 
/**
 * wTLD 0x0007: UTF8, city
 * @param city_ город
 */
public void setCity(String city_){
    this.city = city_;
}
 
/**
 * wTLD 0x0008: UTF8, zip code
 * @param zip_code_ индекс
 */
public void setZipCode(String zip_code_){
    this.zip_code = zip_code_;
}
 
/**
 * wTLD 0x0009: UTF8, address
 * @param address_ адрес
 */
public void setAddress(String address_){
    this.address = address_;
}
 
/**
 * wTLD 0x000A: Word, language code (can be found at the end of this document)
 * @param language_code_ владение языком
 */
public void setLanguageCode(int language_code_){
    this.language_code = language_code_;
}
 
/**
 * wTLD 0x000B: Word, additional language code (can be found at the end of this document)
 * @param additional_language_code_ дополниетельное владение языком
 */
public void setAdditionalLanguageCode(int additional_language_code_){
    this.additional_language_code = additional_language_code_;
}
 
/**
 * wTLD 0x000C: Byte, gender (0x00 - not specified, 0x01 - female, 0x02 - male)
 * @param gender_ пол
 */
public void setGender(byte gender_){
    this.gender = gender_;
}
 
/**
 * wTLD 0x000D: DateTime, birthday
 * @param birthday_ дата рождения
 */
public void setBirthday(long birthday_){
    this.birthday = birthday_;
}
 
/**
 * wTLD 0x000E: UTF8, homepage
 * @param homepage_ домашняя страничка
 */
public void setHomepage(String homepage_){
    this.homepage = homepage_;  
}
 
/**
 * wTLD 0x000F: UTF8, about
 * @param about_ о себе
 */
public void setAbout(String about_){
    this.about = about_;
}
 
/**
 * wTLD 0x0010: UTF8, interests
 * @param interests_ интересы
 */
public void setInterests(String interests_){
    this.interests = interests_;
}
 
/**
 * wTLD 0x0011: UTF8, email
 * @param email_ почта
 */
public void setEmail(String email_){
    this.email = email_;
}
 
/**
 * wTLD 0x0012: UTF8, additional email
 * @param additional_email_ дополнительная почта
 */
public void setAdditionalEmail(String additional_email_){
    this.additional_email = additional_email_;
}
 
/**
 * wTLD 0x0013: UTF8, home phone
 * @param home_phone_ домашний телефон
 */
public void setHomePhone(String home_phone_){
    this.home_phone = home_phone_;
}
 
/**
 * wTLD 0x0014: UTF8, work phone
 * @param work_phone_ рабочий телефон
 */
public void setWorkPhone(String work_phone_){
    this.work_phone = work_phone_;
}
 
/**
 * wTLD 0x0015: UTF8, cellular phone
 * @param cellular_phone_ сотовый телефон
 */
public void setCellularPhone(String cellular_phone_){
    this.cellular_phone = cellular_phone_;
}
 
/**
 * wTLD 0x0016: UTF8, fax number
 * @param fax_number_ факс телефон
 */
public void setFaxNumber(String fax_number_){
    this.fax_number = fax_number_;
}
 
/**
 * wTLD 0x0017: Bool, if True then online status will not be shown in users directory
 * @param online_status_ показывать онлайн?
 */
public void setOnlineStatus(boolean online_status_){
    this.online_status = online_status_;
}
 
/**
 * wTLD 0x0018: UTF8, company
 * @param company_ компания
 */
public void setCompany(String company_){
    this.company = company_;
}
 
/**
 * wTLD 0x0019: UTF8, division/department
 * @param division_department_ подразделение/отдел
 */
public void setDivisionDepartment(String division_department_){
    this.division_department = division_department_;
}
 
/**
 * wTLD 0x001A: UTF8, position
 * @param position_ должность
 */
public void setPosition(String position_){
    this.position = position_;
}
 
}
