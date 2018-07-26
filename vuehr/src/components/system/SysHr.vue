<template>
  <div style="margin-top: 10px" v-loading="fullloading">
    <div style="margin-bottom: 10px;display: flex;justify-content: center;align-items: center">
      <el-input
        placeholder="默认展示部分用户，可以通过用户名搜索更多用户..."
        prefix-icon="el-icon-search"
        size="small"
        style="width: 400px;margin-right: 10px"
        v-model="keywords">
      </el-input>
      <el-button size="small" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
    </div>
    <div style="text-align: left;margin-bottom: 10px;">
      <el-button @click="openAddDlg" icon="el-icon-plus" type="primary" size="mini">添加操作员</el-button>
    </div>
    <div style="display: flex;justify-content: space-around;flex-wrap: wrap;text-align: left">
      <el-table
        :data="hrs"
        stripe
        v-loading="tableLoading"
        size="mini"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <el-table-column
          type="selection"
          width="30">
        </el-table-column>
        <el-table-column
          width="90"
          prop="username"
          label="用户名">
        </el-table-column>
        <el-table-column
          width="70"
          prop="name"
          label="姓名">
        </el-table-column>
        <el-table-column
          width="100"
          prop="phone"
          label="手机号">
        </el-table-column>
        <el-table-column
          width="100"
          prop="telephone"
          label="电话号码">
        </el-table-column>
        <el-table-column
          width="200"
          prop="address"
          label="地址">
        </el-table-column>
        <el-table-column width="150" label="用户角色">
          <template slot-scope="scope">
            <el-tag
              v-for="role in scope.row.roles"
              :key="role.id"
              type="success"
              size="mini"
              style="margin-right: 5px"
              :disable-transitions="false">{{role.nameZh}}
            </el-tag>
            <el-popover
              v-loading="eploading[scope.$index]"
              placement="right"
              title="角色列表"
              width="200"
              @hide="updateHrRoles(scope.row.id,scope.$index)"
              :key="scope.row.id"
              trigger="click">
              <el-select v-model="selRoles" multiple placeholder="请选择角色">
                <el-option
                  v-for="ar in allRoles"
                  :key="ar.id"
                  :label="ar.nameZh"
                  :value="ar.id">
                </el-option>
              </el-select>
              <el-button type="text" icon="el-icon-more" style="color: #09c0f6;padding-top: 0px" slot="reference"
                         @click="loadSelRoles(scope.row.roles,scope.$index)" :disabled="moreBtnState"></el-button>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          width="100"
          prop="remark"
          label="备注">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch
                style="display: inline;margin-left: 5px"
                v-model="scope.row.enabled"
                active-color="#13ce66"
                inactive-color="#aaaaaa"
                :key="scope.row.id"
                @change="switchChange(scope.$index,scope.row)">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column label="编辑" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">编辑
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="删除" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
      <el-form :model="hr" :rules="rules" ref="addHrForm" label-width="100px" label-position="right">
        <div style="text-align: left">
          <el-dialog
            :title="dlgTitle"
            :visible.sync="dialogVisible"
            width="50%"
            @close="emptyDlg"
            :close-on-click-modal="false">
            <el-row>
              <el-col :span="12">
                <div>
                  <el-form-item label="用户名:" prop="username">
                    <el-input v-model="hr.username" size="mini" placeholder="请输入用户名..." :disabled="userval"></el-input>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div>
                  <el-form-item label="姓名:" prop="name">
                    <el-input v-model="hr.name" size="mini" placeholder="请输入姓名..."></el-input>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div>
                  <el-form-item label="密码:" prop="password">
                    <el-input type="password" v-model="hr.password" auto-complete="off" size="mini" placeholder="请输入密码..."></el-input>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div>
                  <el-form-item label="确认密码:" prop="rpassword">
                    <el-input type="password" v-model="hr.rpassword" auto-complete="off" size="mini" placeholder="请再一次输入密码..."></el-input>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div>
                  <el-form-item label="手机号:" prop="phone">
                    <el-input v-model="hr.phone" size="mini" placeholder="请输入手机号..."></el-input>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div>
                  <el-form-item label="固定电话:" prop="telephone">
                    <el-input v-model="hr.telephone" size="mini" placeholder="请输入固定电话号码..."></el-input>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <div>
                  <el-form-item label="地址:" prop="address">
                    <el-input v-model="hr.address" size="mini" placeholder="请输入地址..."></el-input>
                  </el-form-item>
                  <el-form-item label="备注:" prop="remark">
                    <el-input v-model="hr.remark" size="mini" placeholder="请输入备注..."></el-input>
                  </el-form-item>
                  <el-form-item label="角色:" prop="roles">
                    <el-select v-model="hr.roles" value-key="id" multiple placeholder="请选择角色" size="mini" style="width: 230px">
                      <el-option
                        v-for="ar in allRoles"
                        :key="ar.id"
                        :label="ar.nameZh"
                        :value="ar">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="状态：" prop="enabled">
                    <el-switch
                      style="display: inline;margin-left: 5px"
                      v-model="hr.enabled"
                      active-color="#13ce66"
                      inactive-color="#aaaaaa">
                    </el-switch>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <span slot="footer" class="dialog-footer">
              <el-button size="mini" @click="cancelEidt">取 消</el-button>
              <el-button size="mini" type="primary" @click="addHr('addHrForm')">确 定</el-button>
            </span>
          </el-dialog>
        </div>
      </el-form>

    </div>
  </div>
</template>
<script>
  export default{
    data(){
      var validatePass = (rule, value, callback) => {
        if (value !== this.hr.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        keywords: '',
        dialogVisible: false,
        fullloading: false,
        tableLoading: false,
        hrs: [],
        cardLoading: [],
        eploading: [],
        allRoles: [],
        moreBtnState:false,
        selRoles: [],
        selRolesBak: [],
        multipleSelection: [],
        dlgTitle: '',
        userval: false,
        hIndex: 0,
        hr: {
          id: 0,
          username: '',
          name: '',
          password: '',
          rpassword: '',
          telephone: '',
          phone: '',
          address: '',
          enabled: true,
          remark: '',
          roles: [],
        },
        rules: {
          name: [{required: true, message: '必填:姓名', trigger: 'blur'}],
          username: [{required: true, message: '必填:用户名', trigger: 'blur'}],
          password: [{required: true, message: '必填:密码', trigger: 'blur'},
            { min: 5, max: 8, message: '密码长度必须是5-8位', trigger: 'change' }
            ],
          rpassword: [{required: true, message: '必填:确认密码', trigger: 'blur'},
            { validator: validatePass, trigger: 'change' }
            ],
          roles: [{required: true, message: '必填:角色', trigger: 'blur'}],
        }
      }
    },
    mounted: function () {
      this.initCards();
      this.loadAllRoles();
    },
    methods: {
      searchClick(){
        this.initCards();
        this.loadAllRoles();
      },
      handleSelectionChange(val){
        this.multipleSelection = val;
      },
      cancelEidt(){
        this.dialogVisible = false;
        this.emptyDlg();
      },
      addHr(formName) {
        var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (_this.hr.id != 0) {  //更新
              this.tableLoading = true;
              this.putRequest("/system/hr", {
                id: _this.hr.id,
                username: _this.hr.username,
                name: _this.hr.name,
                password: _this.hr.password,
                phone: _this.hr.phone,
                telephone: _this.hr.telephone,
                address: _this.hr.address,
                remark: _this.hr.remark,
                enabled: _this.hr.enabled
              }).then(resp=> {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.updateHrRoles(_this.hr.id,_this.hIndex);
                  //_this.$message({type: data.status, message: data.msg});
                  _this.dialogVisible = false;
                  _this.emptyDlg();
                }
              })
            } else {
              this.tableLoading = true;
              this.postRequest("/system/hr/add", {
                id: 0,
                username: _this.hr.username,
                name: _this.hr.name,
                password: _this.hr.password,
                phone: _this.hr.phone,
                telephone: _this.hr.telephone,
                address: _this.hr.address,
                remark: _this.hr.remark,
                enabled: _this.hr.enabled
              }).then(resp=> {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  if (_this.hr.roles.length != 0) {  //角色选择时
                    var tmpRoles = [];
                    _this.hr.roles.forEach(role=> {
                      tmpRoles.push(role.id)
                    });
                    this.putRequest("/system/hr/roles", {
                      hrId: data.msg,
                      rids: tmpRoles
                    }).then(resp=> {
                      if (resp && resp.status == 200) {
                        var data1 = resp.data;
                        _this.$message({type: data1.status, message: data1.msg});
                      }
                    });
                  }
                  //_this.$message({type: data.status, message: data.msg});
                  _this.dialogVisible = false;
                  _this.emptyDlg();
                  _this.initCards();
                  _this.loadAllRoles();
                }
              })
            }
          }
        });
      },
      updateHrRoles(hrId, index){
        this.moreBtnState = false;
        var _this = this;
        if (this.selRolesBak.length == _this.selRoles.length) {
          for (var i = 0; i < _this.selRoles.length; i++) {
            for (var j = 0; j < this.selRolesBak.length; j++) {
              if (_this.selRoles[i] == this.selRolesBak[j]) {
                this.selRolesBak.splice(j, 1);
                break;
              }
            }
          }
          if (this.selRolesBak.length == 0) {
            return;
          }
        }
        this.eploading.splice(index, 1, true)
        this.putRequest("/system/hr/roles", {
          hrId: hrId,
          rids: this.selRoles
        }).then(resp=> {
          _this.eploading.splice(index, 1, false);
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            if (data.status == 'success') {
              _this.refreshHr(hrId, index);
            }
          }
        });
      },
      emptyDlg(){
        this.hr = {
          id: 0,
          username: '',
          name: '',
          password: '',
          rpassword: '',
          telephone: '',
          phone: '',
          address: '',
          enabled: true,
          remark: '',
          roles: [],
        };
        this.hIndex = 0;
      },
      refreshHr(hrId, index){
        var _this = this;
        _this.cardLoading.splice(index, 1, true)
        this.putRequest("/system/hr/id/" + hrId).then(resp=> {
          _this.cardLoading.splice(index, 1, false)
          _this.hrs.splice(index, 1, resp.data);
        })
      },
      loadSelRoles(hrRoles, index){
        this.moreBtnState = true;
        this.selRoles = [];
        this.selRolesBak = [];
        hrRoles.forEach(role=> {
          this.selRoles.push(role.id)
          this.selRolesBak.push(role.id)
        })
      },
      loadAllRoles(){
        var _this = this;
        this.getRequest("/system/basic/roles").then(resp=> {
          _this.fullloading = false;
          if (resp && resp.status == 200) {
            _this.allRoles = resp.data;
          }
        })
      },
      switchChange(index,row){
        var _this = this;
        _this.cardLoading.splice(index, 1, true)
        this.putRequest("/system/hr/", {
          enabled: row.enabled,
          id: row.id
        }).then(resp=> {
          _this.cardLoading.splice(index, 1, false)
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            if (data.status == 'error') {
              _this.refreshHr(row.id, index);
            }
          } else {
            _this.refreshHr(row.id, index);
          }
        })
      },
      initCards(){
        this.fullloading = true;
        var _this = this;
        var searchWords;
        if (this.keywords === '') {
          searchWords = 'all';
        } else {
          searchWords = this.keywords;
        }
        this.getRequest("/system/hr/" + searchWords).then(resp=> {
          if (resp && resp.status == 200) {
            _this.hrs = resp.data;
            var length = resp.data.length;
            _this.cardLoading = Array.apply(null, Array(length)).map(function (item, i) {
              return false;
            });
            _this.eploading = Array.apply(null, Array(length)).map(function (item, i) {
              return false;
            });
          }
        })
      },
      openAddDlg() {
        this.dlgTitle = "添加操作员";
        this.userval = false;
        this.dialogVisible = true;
      },
      handleEdit(index, row){
        this.dlgTitle = "编辑操作员";
        this.hr = row;
        this.userval = true;
        this.hIndex = index;
        this.dialogVisible = true;
      },
      handleDelete(index, row){
        var _this = this;
        this.$confirm('删除[' + row.name + '], 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.deleteHr(row.id);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      deleteHr(hrId){
        var _this = this;
        this.fullloading = true;
        this.deleteRequest("/system/hr/" + hrId).then(resp=> {
          _this.fullloading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            if (data.status == 'success') {
              _this.initCards();
              _this.loadAllRoles();
            }
          }
        })
      }
    }
  }
</script>
<style>
  .user-info {
    font-size: 12px;
    color: #09c0f6;
  }
</style>
