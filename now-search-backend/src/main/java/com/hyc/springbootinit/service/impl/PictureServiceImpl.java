package com.hyc.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.rholder.retry.Retryer;
import com.hyc.springbootinit.common.ErrorCode;
import com.hyc.springbootinit.exception.BusinessException;
import com.hyc.springbootinit.model.entity.Picture;
import com.hyc.springbootinit.service.PictureService;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import reactor.util.retry.Retry;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hyc
 */
@Service
@Slf4j
public class PictureServiceImpl implements PictureService {

    @Resource
    private Retryer<Document> retry;

    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        String url = "https://www.bing.com/images/search?q=" + searchText + "&go=%E6%90%9C%E7%B4%A2&qs=ds&form=QBIR&first=" + current;
        Document doc = null;
        try {
            doc = retry.call(() -> Jsoup.connect(url).get());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : elements) {
            String m = element.select(".iusc").get(0).attr("m");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            // 获取图片的地址(murl)
            String murl = (String) map.get("murl");
            Picture picture = new Picture();
            picture.setUrl(murl);
            picture.setTitle(title);
            pictureList.add(picture);
            if (pictureList.size() >= pageSize) {
                break;
            }
        }
        Page<Picture> page = new Page<>(current, pageSize);
        page.setRecords(pictureList);
        return page;
    }
}
