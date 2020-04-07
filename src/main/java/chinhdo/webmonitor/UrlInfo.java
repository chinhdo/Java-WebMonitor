package chinhdo.webmonitor;

/** Represent a Url */
public class UrlInfo {
    /** URL to check */
    public String url;

    /** String to check for in response */
    public String checkStr;

    /** True if check was successful */
    public Boolean isOK;

    /** Length of response */
    public Integer length;

    /** How long it took in ms to retrieve URL */
    public Long elapsedMs;

    /** Error message if an error occurred */
    public String error;
}