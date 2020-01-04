package cn.itcast.bos.web.action.transit;

import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.service.transit.InOutStoreService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * User: 李锦卓
 * Time: 2018/9/18 15:35
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class InOutStorageAction extends BaseAction<InOutStorageInfo> {
    @Autowired
    public InOutStoreService inOutStoreService;
    private String transitInfoId;

    public void setTransitInfoId(String transitInfoId) {
        this.transitInfoId = transitInfoId;
    }

    //保存出入库信息
    @Action(value = "inoutstore_save", results = {@Result(name = "success",
            type = "redirect",location = "/pages/transit/transitinfo.html")})
    public String save() {
        inOutStoreService.save(transitInfoId,model);
        return SUCCESS;
    }
}