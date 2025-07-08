package com.ouweicong.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Upload_to_Gitee {
    //码云私人令牌
    public static final String ACCESS_TOKEN = "d7afa00bbb74af6d37ee98368d26d887";

    //码云个人空间名
    public static final String OWNER = "ouweicong";

    //上传指定仓库
    public static final String REPO = "file";

    //上传时指定存放图片路径
    public static final String PATH = DateUtils.getYearMonth() + "/"; //使用到了日期工具类

    //提交描述
    public static final String ADD_MESSAGE = "add img";
    public static final String DEL_MESSAGE = "delete img";

    //API
    /**
     * 新建(POST)、获取(GET)、删除(DELETE)文件：()中指的是使用对应的请求方式
     * %s =>仓库所属空间地址(企业、组织或个人的地址path)  (owner)
     * %s => 仓库路径(repo)
     * %s => 文件的路径(path)
     */
    public static final String API_CREATE_POST = "https://gitee.com/api/v5/repos/%s/%s/contents/%s";

    /**
     * 生成创建(获取、删除)的指定文件路径
     *
     * @param originalFilename
     * @return
     */
    public static String createUploadFileUrl(String originalFilename, String path) {
        //获取文件后缀
        String suffix = FileUtils.getFileSuffix(originalFilename);//使用到了自己编写的FileUtils工具类
        //拼接存储的图片名称
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + suffix;
        //填充请求路径
        String url = String.format(Upload_to_Gitee.API_CREATE_POST, Upload_to_Gitee.OWNER, Upload_to_Gitee.REPO, path + "/" + fileName);
        return url;
    }

    /**
     * 获取创建文件的请求体map集合：access_token、message、content
     *
     * @param multipartFile 文件字节数组
     * @return 封装成map的请求体集合
     */
    public static Map<String, Object> getUploadBodyMap(byte[] multipartFile) {
        Map<String, Object> bodyMap = new HashMap<>(3);
        bodyMap.put("access_token", Upload_to_Gitee.ACCESS_TOKEN);
        bodyMap.put("message", Upload_to_Gitee.ADD_MESSAGE);
        bodyMap.put("content", Base64.encode(multipartFile));
        return bodyMap;
    }

    public static String upload(MultipartFile multipartFile) throws IOException {
        //根据文件名生成指定的请求url
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            return "文件不存在";
        }
        String targetURL = Upload_to_Gitee.createUploadFileUrl(originalFilename, "WeiHuiJuan");
        //请求体封装
        Map<String, Object> uploadBodyMap = Upload_to_Gitee.getUploadBodyMap(multipartFile.getBytes());
        //借助HttpUtil工具类发送POST请求
        System.out.println(targetURL);
        String JSONResult = HttpUtil.post(targetURL, uploadBodyMap);
        //解析响应JSON字符串
        JSONObject jsonObj = JSONUtil.parseObj(JSONResult);
        //请求失败
        if (jsonObj == null || jsonObj.getObj("commit") == null) {
            return "请求失败";
        }
        //请求成功：返回下载地址
        JSONObject content = JSONUtil.parseObj(jsonObj.getObj("content"));
        System.out.println(content.getObj("download_url"));
        String download_url = content.getObj("download_url").toString();
        return download_url;
    }

    public static String delete(String url) throws IOException {

        //路径不存在不存在时
        if(url == null || "".equals(url.trim())){
            return "路径不存在";
        }
        String path = url.split("master/")[1];
        //上传图片不存在时
        if(path == null || "".equals(path.trim())){
            return "上传图片不存在";
        }

        //设置请求路径
        String requestUrl = String.format(Upload_to_Gitee.API_CREATE_POST, Upload_to_Gitee.OWNER, Upload_to_Gitee.REPO, path);
        //获取图片所有信息
        String resultJson = HttpUtil.get(requestUrl);
        if(resultJson == null || resultJson.equals("[]")){
            return "路径不存在";
        }
        JSONObject jsonObject = JSONUtil.parseObj(resultJson);
        if (jsonObject == null) {
            return "Gitee服务器未响应";
        }
        //获取sha,用于删除图片
        String sha = jsonObject.getStr("sha");
        //设置删除请求参数
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("access_token", Upload_to_Gitee.ACCESS_TOKEN);
        paramMap.put("sha", sha);
        paramMap.put("message", Upload_to_Gitee.DEL_MESSAGE);
        //设置删除路径
        requestUrl = String.format(Upload_to_Gitee.API_CREATE_POST, Upload_to_Gitee.OWNER, Upload_to_Gitee.REPO, path);
        //删除文件请求路径
        resultJson = HttpRequest.delete(requestUrl).form(paramMap).execute().body();
//        HttpRequest.put(requestUrl).form(paramMap).execute().body();
        jsonObject = JSONUtil.parseObj(resultJson);
        //请求之后的操作
        if(jsonObject.getObj("commit") == null){
            return "Gitee服务器未响应";
        }
        return "删除成功";
    }
}

class DateUtils {
    public static String getYearMonth() {
        //yyyyMMdd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}

class FileUtils {
    public static String getFileSuffix(String fileName) {
        return fileName.contains(".") ? fileName.substring(fileName.indexOf('.')) : null;
    }
}

