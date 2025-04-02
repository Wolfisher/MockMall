package com.shawnmall.controller.backend;

import com.google.common.collect.Maps;
import com.shawnmall.common.Const;
import com.shawnmall.common.ServerResponse;
import com.shawnmall.pojo.Product;
import com.shawnmall.pojo.User;
import com.shawnmall.service.IFileService;
import com.shawnmall.service.IProductService;
import com.shawnmall.service.IUserService;
import com.shawnmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @program: ShawnMall
 * @description: Controller for product management
 * @author: Shawn Li
 * @create: 2018-09-11 13:58
 **/

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    //Save product
    @RequestMapping("product_save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithErrorMsg("Admin is not logged in, please login now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //business logic for add or update product
            return iProductService.saveUpdateProduct(product);
        } else {
            return ServerResponse.createWithErrorMsg("Need to be admin to do the operation");
        }
    }

    //Change the product sales status
    @RequestMapping("set_sales_status.do")
    @ResponseBody
    public ServerResponse setSalesStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithErrorMsg("Admin is not logged in, please login now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //business logic for add or update product
            return iProductService.setSalesStatus(productId, status);
        } else {
            return ServerResponse.createWithErrorMsg("Need to be admin to do the operation");
        }
    }

    //Get product details
    @RequestMapping("get_product_details.do")
    @ResponseBody
    public ServerResponse getProductDetails(HttpSession session, Integer productId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithErrorMsg("Admin is not logged in, please login now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //business logic for add or update product
            return iProductService.manageProductDetails(productId);
        } else {
            return ServerResponse.createWithErrorMsg("Need to be admin to do the operation");
        }
    }


    //Get product lists
    @RequestMapping("get_product_lists.do")
    @ResponseBody
    public ServerResponse getProductList(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithErrorMsg("Admin is not logged in, please login now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //business logic：
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createWithErrorMsg("Need to be admin to do the operation");
        }
    }

    //Back-end Product search
    @RequestMapping("product_search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName, Integer productId,
                                         @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithErrorMsg("Admin is not logged in, please login now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //business logic：
            return iProductService.productSearch(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createWithErrorMsg("Need to be admin to do the operation");
        }
    }

    //Using SpringMVC to upload files
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithErrorMsg("Admin is not logged in, please login now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //business logic：
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            return ServerResponse.createWithSuccess(fileMap);
        } else {
            return ServerResponse.createWithErrorMsg("Need to be admin to do the operation");
        }
    }

    //rich text and image uploading
    //Using the official format of simditor
    //        {
    //            "success": true/false,
    //                "msg": "error message", # optional
    //            "file_path": "[real file path]"
    //        }

    @RequestMapping("rich_text_image_upload.do")
    @ResponseBody
    public Map richTextImageUpload(HttpSession session,
                                   @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                   HttpServletRequest request, HttpServletResponse response) {
        //log in check
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "Please Login as admin");
            return resultMap;
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //business logic：
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);

            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "file upload failure");
                return resultMap;
            }

            String url = PropertiesUtil.getProperty("ftp.server.http.prefix" + targetFileName);

            resultMap.put("success", true);
            resultMap.put("msg", "file upload success");
            resultMap.put("file_path", url);
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");

            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "Need to be admin to do the operation");
            return resultMap;
        }
    }

}
