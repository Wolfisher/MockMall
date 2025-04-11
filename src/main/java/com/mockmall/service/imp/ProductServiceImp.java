package com.mockmall.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mockmall.common.Const;
import com.mockmall.common.ResponseCode;
import com.mockmall.common.ServerResponse;
import com.mockmall.dao.CategoryMapper;
import com.mockmall.dao.ProductMapper;
import com.mockmall.pojo.Category;
import com.mockmall.pojo.Product;
import com.mockmall.service.ICategoryService;
import com.mockmall.service.IProductService;
import com.mockmall.util.DateTimeUtil;
import com.mockmall.util.PropertiesUtil;
import com.mockmall.vo.ProductDetailVo;
import com.mockmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: ShawnMall
 * @description: implementation of product service
 * @author: Shawn Li
 * @create: 2018-09-11 14:06
 **/

@Service("iProductService")
public class ProductServiceImp implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;

    //Save or update the product
    public ServerResponse saveUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int resultCount = productMapper.updateByPrimaryKey(product);
                if (resultCount > 0) {
                    return ServerResponse.createWithSuccessMsg("Product is updated");
                }
                return ServerResponse.createWithErrorMsg("Updating Product failed");

            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0){
                    return ServerResponse.createWithSuccessMsg("New product is added to the database");
                }
                return ServerResponse.createWithErrorMsg("Adding new Product failed");
            }

        }
        return ServerResponse.createWithErrorMsg("Parameter is not valid for new or updated product");
    }

    //Change the product sales status
    public ServerResponse setSalesStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createWithError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if(resultCount > 0) {
            return ServerResponse.createWithSuccessMsg("Set the sales status success");
        }
        return ServerResponse.createWithErrorMsg("Can not set product sales status");
    }

    //Manage the product information details
    public ServerResponse<ProductDetailVo> manageProductDetails(Integer productId) {
        if (productId == null) {
            return ServerResponse.createWithError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (productId == null) {
            return ServerResponse.createWithErrorMsg("Product is no longer exit");
        }
        //Order:  VO(value object) --> [pojo->BO(business object) -> VO(view object)]
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createWithSuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());


        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix",
                "http://img.shawnmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    public ServerResponse getProductList(int pageNum, int pageSize) {
        //start page- to start
        PageHelper.startPage(pageNum, pageSize);

        //search logic with your own sql logic
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = new ArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        //page-helper- end
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createWithSuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();

        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix",
                "http://img.shawnmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());

        return productListVo;

    }

    public ServerResponse<PageInfo> productSearch(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByProductNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = new ArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        //page- helper- end
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createWithSuccess(pageResult);
    }

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createWithError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (productId == null) {
            return ServerResponse.createWithErrorMsg("Product is no longer exit");
        }

        if (product.getStatus() != Const.productStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createWithErrorMsg("Product is not online");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createWithSuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createWithError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }

        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //return an empty set, do not report mistake
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createWithSuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenByID(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.price_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }

        List<Product> productList = productMapper.selectByProductNameAndCategoryId(StringUtils.isBlank(keyword) ? null : keyword,
                categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServerResponse.createWithSuccess(pageInfo);
    }

}
