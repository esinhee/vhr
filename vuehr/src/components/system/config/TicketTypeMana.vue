<template>
  <div>
    <div style="text-align: left">
      <el-input
        placeholder="添加工单类型..."
        size="mini"
        @keyup.enter.native="addTicketType"
        style="width: 300px;"
        prefix-icon="el-icon-plus"
        v-model="typeName">
      </el-input>
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="addTicketType(1)">添加</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-tree
        :props="defaultProps"
        :data="typeData"
        ref="tree"
        :filter-node-method="filterNode"
        v-loading="loading"
        default-expand-all
        style="width: 500px;margin-top: 10px"
        :render-content="renderContent">
      </el-tree>
    </div>
    <div style="text-align: left;">
      <el-dialog
        title="编辑工单类型"
        :visible.sync="dialogVisible"
        width="25%">
        <div style="margin-top: 10px">
          <span>上级类型名称</span>
          <el-input v-model="pTypeName" size="mini" placeholder="请输入新的工单类型名称..."></el-input>
        </div>
        <div style="margin-top: 10px">
          <span>工单类型名称</span>
          <el-input v-model="updateTypeName" size="mini" placeholder="请输入新的工单类型名称..."></el-input>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" size="mini" @click="addTicketType">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<script>
  export default{
    mounted: function () {
      this.loading = true;
      this.loadTreeData();
    },
    methods: {
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      updatePosNameExec(){
        if (!this.isNotNullORBlank(this.updatePosName)) {
          this.$message.warning('工单类型名称不能为空!');
          return;
        }
        this.loading = true;
        var _this = this;
        this.putRequest(this.state=='position'?"/system/basic/position":"/system/basic/joblevel", {name: this.updatePosName, id: this.updatePosId,titleLevel:this.updateTitleLevel}).then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            this.dialogVisible = false;
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            _this.loadTableData();
          }
        })
      },
      setDataToTree(treeData,pId,respData){
        for(var i=0;i<treeData.length;i++) {
          var td = treeData[i];
          if(td.id==pId) {
            treeData[i].children=treeData[i].children.concat(respData);
            return;
          }else{
            this.setDataToTree(td.children, pId, respData);
          }
        }
      },
      addTicketType(lev){
        var _this = this;
        var pTypeName = _this.pTypeName;
        var pTypeId = _this.pTypeId;
        var typename = '';
        if (lev == 1) {
          if (!this.isNotNullORBlank(this.typeName)) {
            this.$message.warning('工单类型名称不能为空!');
            return;
          }
          pTypeId = 0;
          pTypeName = "";
          typename = _this.typeName;
        } else {
          if (!this.isNotNullORBlank(_this.updateTypeName)) {
            this.$message.warning('工单类型名称不能为空!');
            return;
          }
          typename = _this.updateTypeName;
        }
        _this.loading = true;
        _this.postRequest("/system/config/tkttype", {
          typename: typename,
          parentid: pTypeId,
          bz: 1
        }).then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            _this.loadTableData();
            _this.typeName = '';
            _this.pTypeName = '';
            _this.pTypeId = -1;
            _this.updateTypeName = '';
            _this.updateTypeId = -1;
            _this.dialogVisible = false;
            _this.$message({type: data.status, message: "添加成功!"});
            _this.setDataToTree(_this.typeData,data.msg.parentid,data.msg)
          }
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      handleEdit(index, row){
        this.updateTypeName = row.name;
        this.updateTypeId = row.id;
        this.dialogVisible = true;
      },
      handleDelete(index, row){
        var _this = this;
        this.$message({
          type: 'warnning',
          message: '删除功能停用！'
        });
        return;
        this.$confirm('删除[' + row.name + '], 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.doDelete(row.id);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      doDelete(ids){
        var _this = this;
        _this.loading = true;
        var url = this.state == 'position' ? '/system/basic/position/' : '/system/basic/joblevel/';
        this.deleteRequest(url + ids).then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.loadTableData();
          }
        })
      },
      loadTreeData(){
        var _this = this;
        _this.loading = true;
        this.getRequest("/system/config/tttoplist/0").then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.typeData = resp.data;
          }
        })
      },
      /*loadTableData(){
        var _this = this;
        this.loading = true;
        this.getRequest("/system/config/tkttypelist").then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.typeData = resp.data;
          }
        })
      },*/
      openEditDlg(data, event) {
        this.dialogVisible = true;
        this.pTypeName = data.typename;
        this.pTypeId = data.parentid;
        this.updateTypeName = '';
        event.stopPropagation()
      },
      showNewView(data, event){
        //this.loadAllDeps();
        this.dialogVisible = true;
        this.pTypeName = data.typename;
        this.pTypeId = data.id;
        this.updateTypeName = '';
        event.stopPropagation()
      },
      renderContent(h, {node, data, store}) {
        return (
          <span style="flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;">
          <span>
          <span>{node.label}</span>
        </span>
        <span>
        <el-button disabled={data.seq<3 ? false : true} style="font-size: 12px;" type="primary" size="mini" style="padding:3px" on-click={ (event) => this.showNewView(data,event) }>添加</el-button>
        <el-button style="font-size: 12px;" type="danger" size="mini" style="padding:3px" on-click={ (event) => this.openEditDlg(data,event) }>编辑</el-button>
        <el-button style="font-size: 12px;" type="danger" size="mini" style="padding:3px" on-click={ (event) => this.deleteDep(data,event) }>停用</el-button>
        <el-button style="font-size: 12px;" type="danger" size="mini" style="padding:3px" on-click={ (event) => this.deleteDep(data,event) }>删除</el-button>
        </span>
        </span>);
      }
    },
    data(){
      return {
        typeName: '',
        updateTypeName: '',
        updateTypeId: -1,
        loading: false,
        dialogVisible: false,
        multipleSelection: [],
        type: [],
        typeData: [],
        pTypeName: '',
        pTypeId: '',
        defaultProps: {
          label: 'typename',
          isLeaf: 'leaf',
          children: 'children'
        }
      }
    },
  }
</script>
