
package ru.gdgkazan.footbalproject.model.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class TeamResponse {

    @SerializedName("_links")
    private Links links;

    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private String code;

    @SerializedName("shortName")
    private String shortName;

    @SerializedName("squadMarketValue")
    private String squadMarketValue;

    @SerializedName("crestUrl")
    private String crestUrl;

    /**
     * 
     * @return
     *     The links
     */
    @NonNull
    public Links getLinks() {
        return links;
    }

    /**
     * 
     * @param links
     *     The _links
     */
    public void setLinks(@NonNull Links links) {
        this.links = links;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * @param shortName
     *     The shortName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 
     * @return
     *     The squadMarketValue
     */
    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    /**
     * 
     * @param squadMarketValue
     *     The squadMarketValue
     */
    public void setSquadMarketValue(String squadMarketValue) {
        this.squadMarketValue = squadMarketValue;
    }

    /**
     * 
     * @return
     *     The crestUrl
     */
    public String getCrestUrl() {
        return crestUrl;
    }

    /**
     * 
     * @param crestUrl
     *     The crestUrl
     */
    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

}
