<template>
  <div>
    <div style="text-align: left">
      <el-input
        placeholder="输入科室搜索..."
        style="width: 380px;margin: 0px;padding: 0px;"
        size="mini"
        prefix-icon="el-icon-search"
        v-model="keywords">
      </el-input>
    </div>
    <div style="text-align: left;margin-top: 10px;">
      <el-button @click="newTopDep" icon="el-icon-plus" type="primary" size="mini">添加一级科室</el-button>
    </div>
    <div style="display: flex;">
      <el-tree
        :props="defaultProps"
        :data="treeData"
        ref="tree"
        :filter-node-method="filterNode"
        v-loading="treeLoading"
        default-expand-all
        style="width: 500px;margin-top: 10px"
        :render-content="renderContent">
      </el-tree>
      <transition name="el-zoom-in-center">
        <div v-show="showDlg" style="text-align: left;margin-left: 20px;padding: 10px;">
          <el-card>
            <div slot="header">
              <span>科室</span>
            </div>
            <div>
              <div>
                <span>上级科室</span>
                <el-select v-model="pDep" style="width: 200px" placeholder="请选择" size="mini" :disabled="true">
                  <el-option
                    v-for="item in allDeps"
                    :key="item.kid"
                    :label="item.kname"
                    :value="item.kid">
                  </el-option>
                </el-select>
              </div>
              <div style="margin-top: 10px">
                <span>科室编号</span>
                <el-input size="mini" style="width: 200px;" v-model="depId" placeholder="请输入科室编号..." @keyup.enter.native="addDep"></el-input>
              </div>
              <div style="margin-top: 10px">
                <span>科室名称</span>
                <el-input size="mini" style="width: 200px;" v-model="depName" placeholder="请输入科室名称..." @keyup.enter.native="addDep"></el-input>
              </div>
              <div style="margin-top: 10px">
                <el-button size="small" @click="showDlg = false">取消</el-button>
                <el-button size="small" type="primary" @click="addDep">添加</el-button>
              </div>
            </div>
          </el-card>

        </div>
      </transition>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        keywords: '',
        dep1Name: '',
        depId: '',
        depName: '',
        treeLoading: false,
        dialogVisible: false,
        showDlg: false,
        allDeps: [],
        pDep: '',
        pinyin: '',
        treeData: [],
        defaultProps: {
          label: 'kname',
          isLeaf: 'leaf',
          children: 'children'
        }
      }
    },
    mounted: function () {
      this.treeLoading = true;
      this.loadTreeData();
    },
    watch: {
      keywords(val) {
        this.$refs.tree.filter(val);
      }
    },
    methods: {
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      newTopDep(data) {
        this.depName = '';
        this.pDep = 0;
        this.showDlg = true;
      },
      openEditDlg(data, event) {
        this.loadAllDeps();
        this.showDlg = true;
        this.pDep = data.parentid;
        this.depId = data.kid;
        this.depName = data.kname;
        event.stopPropagation();
      },
      loadTreeData(){
        var _this = this;
        _this.treeLoading = true;
        this.getRequest("/system/config/ksplist/0").then(resp=> {
          _this.treeLoading = false;
          if (resp && resp.status == 200) {
            _this.treeData = resp.data;
          }
        })
      },
      setDataToTree(treeData,pId,respData){
        for(var i=0;i<treeData.length;i++) {
          var td = treeData[i];
          if(td.kid==pId) {
            treeData[i].children=treeData[i].children.concat(respData);
            return;
          }else{
            this.setDataToTree(td.children, pId, respData);
          }
        }
      },
      addDep(){
        var _this = this;
        this.dialogVisible = false;
        this.treeLoading = true;
        this.postRequest("/system/config/addkeshi", {
          kid: this.depId,
          kname: this.depName,
          parentid: this.pDep,
          seq: 1,
          pinyin: ''
        }).then(resp=> {
          _this.treeLoading = false;
          if (resp && resp.status == 200) {
            var respData = resp.data;
            _this.depName = '';
            _this.depId = '';
            _this.showDlg = false;
            _this.$message({type: respData.status, message: "添加成功!"});
            _this.setDataToTree(_this.treeData,_this.pDep,respData.msg)
          }
        })
      },
      loadAllDeps(){
        var _this = this;
        this.getRequest("/system/config/kslist").then(resp=> {
          if (resp && resp.status == 200) {
            _this.allDeps = resp.data;
          }
        });
      },
      showAddDepView(data, event){
        this.loadAllDeps();
        //this.dialogVisible = true;
        this.showDlg = true;
        this.pDep = data.kid;
        this.depId = '';
        this.depName = '';
        event.stopPropagation()
      },
      deleteDep(data, event){
        var _this = this;
        if (data.children.length>0) {
          this.$message({
            message: '该部门下尚有其他部门，不能被删除!',
            type: 'warning'
          });
        } else {
          this.$confirm('删除[' + data.kname + ']部门, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            _this.treeLoading = true;
            _this.deleteRequest("/system/config/dep/" + data.kid).then(resp=> {
              _this.treeLoading = false;
              if (resp && resp.status == 200) {
                var respData = resp.data;
                _this.$message({
                  message: respData.msg,
                  type: respData.status
                });
                _this.deleteLocalDep(_this.treeData, data);
              }
            });
          }).catch(() => {
            _this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        }
        event.stopPropagation()
      },
      deleteLocalDep(treeData,data){
        for(var i=0;i<treeData.length;i++) {
          var td = treeData[i];
          if(td.kid==data.kid) {
            treeData.splice(i, 1);
            return;
          }else{
            this.deleteLocalDep(td.children, data);
          }
        }
      },
      changeStatus(data, event) {
        var _this = this;
        if (data.enabled == 0 && data.children.length > 0) {
          this.$message({
            message: '该部门下尚有其他部门，不能被停用!',
            type: 'warning'
          });
        } else {
          const params = {};
        }

      },
      renderContent(h, {node, data, store}) {
        return (
          <span style="flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;">
          <span>
          <span>【{data.kid}】{node.label}</span>
        </span>
        <span>
        <el-button disabled={data.seq<3 ? false : true} style="font-size: 12px;" type="primary" size="mini" style="padding:3px" on-click={ (event) => this.showAddDepView(data,event) }>添加</el-button>
        <el-button style="font-size: 12px;" type="danger" size="mini" style="padding:3px" on-click={ (event) => this.deleteDep(data,event) }>停用</el-button>
        </span>
        </span>);
      }
    }
  };
</script>
