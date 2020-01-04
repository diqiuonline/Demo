//public是对axios的工具类的封装 定义了http请求方法
import http from './../../../base/api/public'
import querystring from 'querystring'
let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre
/*页面列表*/
export const page_list  = (page,size,params) => {
  //将json 对象转为key/value对象
  let query = querystring.stringify(params);
  return http.requestQuickGet(apiUrl+'/cms/page/list/' + page + '/' + size+'/?'+query)
}
/*增加*/
export const page_add  = params => {
  return http.requestPost(apiUrl+'/cms/page/add/',params)
}
/*根据id查找*/
export const page_get  = id => {
  return http.requestQuickGet(apiUrl+'/cms/page/get/'+id)
}
/*修改*/
export const page_edit  = (id,params) => {
  return http.requestPut(apiUrl+'/cms/page/edit/'+id,params)
}
/*删除*/
export const page_delete  = id => {
  return http.requestDelete(apiUrl + '/cms/page/delete/' + id)
}
/*站点列表*/
export const site_list  = () => {
  return http.requestQuickGet(apiUrl+'/cms/site/list/')
}
/*模板列表*/
export const template_list  = () => {
  return http.requestQuickGet(apiUrl+'/cms/template/list/')
}
/*页面发布*/
export const page_postPage = pageId => {
  return http.requestPost(apiUrl + '/cms/page/postPage/' + pageId);
};
