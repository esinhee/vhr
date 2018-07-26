package org.sang.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventType implements Serializable {
    private Integer id;

    private String typename;

    private Integer parentid;

    private String level;

    private Integer seq;

    private String pinyin;

    private Integer bz;

    private List<EventType> children = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename == null ? null : typename.trim();
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
    }

    public Integer getBz() {
        return bz;
    }

    public void setBz(Integer bz) {
        this.bz = bz;
    }

    public List<EventType> getChildren() {
        return children;
    }

    public void setChildren(List<EventType> children) {
        this.children = children;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", typename=").append(typename);
        sb.append(", parentid=").append(parentid);
        sb.append(", level=").append(level);
        sb.append(", seq=").append(seq);
        sb.append(", pinyin=").append(pinyin);
        sb.append(", bz=").append(bz);
        sb.append(", children=").append(children);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}