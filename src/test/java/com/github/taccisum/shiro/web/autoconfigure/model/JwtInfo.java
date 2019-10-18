package com.github.taccisum.shiro.web.autoconfigure.model;

/**
 * Created By @author zhouyuhang@deepexi.com on @since 2019/10/17
 * <p></p>
 **/
public class JwtInfo {
    private String nickname;
    private boolean admin;
    private Long userId;
    private String channel;

    public String getNickname() {
        return nickname;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
