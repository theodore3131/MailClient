package JavaBean.Entity;

import java.io.Serializable;

/**
 * @author YukonChen
 * 用户信息类
 * 查找朋友时，仿照QQ将关键字按照备注名、用户名、用户id搜索三次
 * 通过HashSet去重
 * 但是显示时，返回值是如果用户类List，则搜索结果无法显示朋友类的备注名
 * 返回值是如果朋友类List，则搜索结果无法显示用户类的用户名
 *
 * 所以用用户信息类将朋友类（朋友id）和关键字结果（备注名|用户名|朋友id）封装起来
 */
public class FriendInfo implements Serializable {

    private String friendId;
    private String keywordRst;

    public FriendInfo(String friendId, String keywordRst){
        this.friendId = friendId;
        this.keywordRst = keywordRst;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getkeywordRst() {
        return keywordRst;
    }
}
