package com.hyc.springbootinit.model.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author hyc
 */
@Data
public class VideoVO implements Serializable {

    /**
     * 视频的地址
     */
    private String arcurl;

    /**
     * 视频封面
     */
    private String pic;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 视频up主
     */
    private String author;

    /**
     * 视频上传时间
     */
    private Integer pubdate;

    private String upic;

    private static final long serialVersionUID = 7037843325406822290L;
}
