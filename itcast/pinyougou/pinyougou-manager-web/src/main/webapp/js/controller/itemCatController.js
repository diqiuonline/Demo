 //控制层 
 app.controller('itemCatController', function ($scope, $controller, itemCatService) {

     $controller('baseController', {$scope: $scope});//继承

     //读取列表数据绑定到表单中
     $scope.findAll = function () {
         itemCatService.findAll().success(
             function (response) {
                 $scope.list = response;
             }
         );
     }

     //分页
     $scope.findPage = function (page, rows) {
         itemCatService.findPage(page, rows).success(
             function (response) {
                 $scope.list = response.rows;
                 $scope.paginationConf.totalItems = response.total;//更新总记录数
             }
         );
     }

     //查询实体
     $scope.findOne = function (id) {
         itemCatService.findOne(id).success(
             function (response) {
                 $scope.entity = response;
             }
         );
     }

     //保存
     $scope.save = function () {
         var serviceObject;//服务层对象
         if ($scope.entity.id != null) {//如果有ID
             serviceObject = itemCatService.update($scope.entity); //修改
         } else {
             //赋予上级id
             $scope.entity.parentId = $scope.parentId;
             serviceObject = itemCatService.add($scope.entity);//增加
         }
         serviceObject.success(
             function (response) {
                 if (response.success) {
                     //重新查询
                     $scope.findByParentId($scope.parentId)
                 } else {
                     alert(response.message);
                 }
             }
         );
     }


     //批量删除
     $scope.dele = function () {
         //获取选中的复选框
         itemCatService.dele($scope.selectIds).success(
             function (response) {
                 if (response.success) {
                     $scope.findByParentId($scope.parentId);//刷新列表
                     $scope.selectIds = [];
                 }
             }
         );
     }

     $scope.searchEntity = {};//定义搜索对象

     //搜索
     $scope.search = function (page, rows) {
         itemCatService.search(page, rows, $scope.searchEntity).success(
             function (response) {
                 $scope.list = response.rows;
                 $scope.paginationConf.totalItems = response.total;//更新总记录数
             }
         );
     };
     $scope.parentId = 0; //上级id
     //根据id查询上级列表
     $scope.findByParentId = function (parentId) {
         //查询时记住上级id
         $scope.parentId = parentId;
         itemCatService.findByParentId(parentId).success(
             function (response) {
                 $scope.list = response;
             }
         );
     };
     $scope.grade = 1;//默认等级是1
     //设置级别
     $scope.setGrade = function (value) {
         $scope.grade = value;
     };
     //读取列表
     $scope.selectList = function (p_entity) {
         if ($scope.grade == 1) { //如果为1级
             $scope.entity_1 = null;
             $scope.entity_2 = null;
         }
         if ($scope.grade == 2) { //如果为2级
             $scope.entity_1 = p_entity;
             $scope.entity_2 = null;
         }
         if ($scope.grade == 3) {  //如果为3级
             $scope.entity_2 = p_entity;
         }
         $scope.findByParentId(p_entity.id); //查询此级下级列表
     };

     //查询时记录上级id

 });
