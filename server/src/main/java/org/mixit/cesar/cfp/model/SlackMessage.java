package org.mixit.cesar.cfp.model;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 28/01/16.
 */
public class SlackMessage {

    private String channel;
    private String text;
    private String username;
    private String icon_emoji;
    private String icon_url;

    public String getChannel() {
        return channel;
    }

    public SlackMessage setChannel(String channel) {
        this.channel = "#" + channel;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SlackMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getText() {
        return text;
    }

    public SlackMessage setText(String text) {
        this.text = text;
        return this;
    }

    public String getIcon_emoji() {
        return icon_emoji;
    }

    public SlackMessage setIcon_emoji(String icon_emoji) {
        this.icon_emoji = ":" + icon_emoji + ":";
        return this;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public SlackMessage setIcon_url(String icon_url) {
        this.icon_url = icon_url;
        return this;
    }
}
