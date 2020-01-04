<template>
  <div>
    <!--查询表单-->
    <el-form :model="params">
      <el-select v-model="params.siteId" placeholder="请选择站点">
        <el-option
          v-for="item in siteList"
          :key="item.siteId"
          :label="item.siteName"
          :value="item.siteId">
        </el-option>
      </el-select>
      页面别名：<el-input v-model="params.pageAliase" style="width:100px"></el-input>
      <el-button type="primary" v-on:click="query"  size="small">查询</el-button>
      <router-link class="mui-tab-item" :to="{path:'/cms/page/add',query:{
        page:this.params.page,
        siteId:this.params.siteId
      }}">
        <el-button type="primary" size="small">新增页面</el-button>
      </router-link>
    </el-form>

    <el-table
      :data="list"
      stripe
      style="width: 100%">
      <el-table-column type="index"  width="60">
      </el-table-column>
      <el-table-column prop="pageName" label="页面名称" width="120">
      </el-table-column>
      <el-table-column prop="pageAliase" label="别名" width="120">
      </el-table-column>
      <el-table-column prop="pageType" label="页面类型" width="95">
      </el-table-column>
      <el-table-column prop="pageWebPath" label="访问路径" width="250">
      </el-table-column>
      <el-table-column prop="pagePhysicalPath" label="物理路径" width="250">
      </el-table-column>
      <el-table-column prop="pageCreateTime" label="创建时间" width="180" >
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="page">
          <el-button
            size="small"type="text"
            @click="edit(page.row.pageId)">编辑
          </el-button>
          <el-button
            size="small"type="text"
           @click="del(page.row.pageId)">删除
          </el-button>
          <el-button
            size="small"type="text"
            @click="perview(page.row.pageId)">
            页面预览
          </el-button>
          <el-button
            size="small"type="primary" plain
            @click="postPage(page.row.pageId)">
            页面发布
          </el-button>
        </template>
</el-table-column>
    </el-table>
    <el-pagination
      layout="prev, pager, next"
      :page-size="this.params.size"
      v-on:current-change="changePage"
      :total="total" :current-page="this.params.page" style="float:right;">
    </el-pagination>
  </div>
</template>
<script>
  /*编写页面静态部分，即model及vm部分。*/
  import * as cmsApi from '../api/cms'
  export default {
    data() {
      return {
        siteList:[], //站点列表
        list: [],
        total:50,
        params:{
          siteId:'',
          pageAliase:'',
          page:1,  //页码
          size:10   //每页显示个数
        }
      }
    },
    methods:{
      //分页查询
      changePage:function () {
        this.query()
      },
      query:function () {
        cmsApi.page_list(this.params.page,this.params.size,this.params).then((res) =>{
          //将res结果数据fu赋值给数据模型对象
          this.list = res.queryResult.list;
          this.total = res.queryResult.total;
        })
      },
      changePage(page){
        this.params.page = page;
        this.query()
      },
      querySite:function () {
        cmsApi.site_list().then((res) =>{
          this.siteList = res.queryResult.list;
        })
      },
      edit:function (pageId) {
        this.$router.push({
          path:'/cms/page/edit/'+pageId,query:{
            page:this.params.page,
            siteId:this.params.siteId
          }
        })
      },
      del:function (pageId) {
        this.$confirm('确认删除此页面吗?', '提示', {}).then(() => {
          //调用服务端接口
          cmsApi.page_delete(pageId).then((res) => {
            if (res.success) {
              this.$message.success("删除成功");
              //刷新页面
              this.query();
            } else {
              this.$message.error("删除失败")
            }
          })
        })
      },
      perview:function (pageId) {
        window.open("http://www.xuecheng.com/cms/preview/"+pageId)
      },
      postPage: function (pageId) {
        this.$confirm('确认发布页面吗','提示',{}).then(() => {
          cmsApi.page_postPage(pageId).then((res) => {
            if (res.success){
              this.$message.success("发布成功，请稍后查看结果");
              //刷新页面
              this.query();
            }else {
              this.$message.error('发布失败');
            }
          });
        });
      }
    },
    mounted(){
      this.query()
      this.querySite()
    },
    created(){
      //从路由上获取参数
      this.params.page=Number.parseInt(this.$route.query.page||1);
      this.params.siteId = this.$route.query.siteId||'';
    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/
</style>
