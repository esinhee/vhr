package org.sang.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Keshi implements Serializable {
    private String kid;

    private String kname;

    private String parentid;

    private String level;

    private Integer seq;

    private String pinyin;

    private Integer enabled;
    
    //存储过程执行结果
    private Integer result;
    
    private List<Keshi> children = new ArrayList<>();

    public List<Keshi> getChildren() {
		return children;
	}

	public void setChildren(List<Keshi> children) {
		this.children = children;
	}

	private static final long serialVersionUID = 1L;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid == null ? null : kid.trim();
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname == null ? null : kname.trim();
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
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

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", kid=").append(kid);
        sb.append(", kname=").append(kname);
        sb.append(", parentid=").append(parentid);
        sb.append(", level=").append(level);
        sb.append(", seq=").append(seq);
        sb.append(", pinyin=").append(pinyin);
        sb.append(", enabled=").append(enabled);
        sb.append(", children=").append(children);
        sb.append(", result=").append(result);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}