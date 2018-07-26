<template>
  <div class="fillcontain">
    <header class="admin_title">管理员信息</header>
    <div class="admin_set">
      <ul>
        <li>
          <span>用 户 名：</span><span>{{user.username}}</span>
        </li>
        <li>
          <span>密    码：</span><el-button @click="dialogVisible = true" icon="el-icon-d-caret" type="primary" size="mini">修改密码</el-button><span>
        </span>
        </li>
        <li>
          <span>姓    名：</span><span>{{user.name}}</span>
        </li>
        <li>
          <span>手 机 号：</span><span>{{user.phone}}</span>
        </li>
        <li>
          <span>联系电话：</span><span>{{user.telephone}}</span>
        </li>
        <li>
          <span>地    址：</span><span>{{user.address}}</span>
        </li>
        <li>
          <span>管理员ID：</span><span>{{user.id}}</span>
        </li>
        <li>
          <span>更换头像：</span>
          <span>
            <el-upload
              class="avatar-uploader"
              style="display: block;"
              action="/personnel/avator"
              :show-file-list="false"
              :on-success="uploadImg"
              :before-upload="beforeImgUpload">
              <img v-if="user.userface" :src="user.userface" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </span>
        </li>
      </ul>
    </div>
    <div style="text-align: left">
      <el-dialog
        title="修改密码"
        :visible.sync="dialogVisible"
        width="50%">
        <el-row>
          <el-col :span="12">
            <el-form :model="psfrm" :rules="rules" ref="psfrm" label-width="110px" class="demo-ticketFrm" label-position="right">
              <el-form-item label="原始密码:" prop="opassword">
                <el-input type="password" v-model="psfrm.opassword" auto-complete="off" size="mini" placeholder="需要验证原始密码..."></el-input>
              </el-form-item>
              <el-form-item label="新的密码:" prop="npassword">
                <el-input type="password" v-model="psfrm.npassword" auto-complete="off" size="mini" placeholder="请输入新密码..."></el-input>
              </el-form-item>
              <el-form-item label="确认密码:" prop="rpassword">
                <el-input type="password" v-model="psfrm.rpassword" auto-complete="off" size="mini" placeholder="请再一次输入新密码..."></el-input>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="modifyPassword('psfrm')">确 定</el-button>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  export default {
    data(){
      var validatePass = (rule, value, callback) => {
        if (value !== this.psfrm.npassword) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        baseUrl: '',
        baseImgPath: '',
        dialogVisible: false,
        psfrm: {
          opassword: '',
          npassword: '',
          rpassword: '',
        },
        rules: {
          opassword: [{required: true, message: '必填:原始密码', trigger: 'blur'},
          ],
          npassword: [{required: true, message: '必填:新密码', trigger: 'blur'},
            { min: 5, max: 8, message: '密码长度必须是5-8位', trigger: 'change' }
          ],
          rpassword: [{required: true, message: '必填:确认密码', trigger: 'blur'},
            { validator: validatePass, trigger: 'change' }
          ],
        }
      }
    },
    computed: {
      user(){
        return this.$store.state.user;
      },
      routes(){
        return this.$store.state.routes
      }
    },
    methods: {
      uploadImg(res, file) {
        if (res.status == 'success') {
          this.$message.error('上传图片成功！');
        }else{
          this.$message.error('上传图片失败！');
        }
      },
      beforeImgUpload(file) {
        const isRightType = (file.type === 'image/jpeg') || (file.type === 'image/png');
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isRightType) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isRightType && isLt2M;
      },
      modifyPassword(frm) {
        var _this = this;
        this.$refs[frm].validate((valid) => {
          if (valid) {
            const params = {
              opwd: _this.psfrm.opassword,
              npwd: _this.psfrm.npassword,
            }
            _this.postRequest('/personnel/resetpass', params).then(resp=>{
              if (resp && resp.status == 200) {
                var data = resp.data;
                _this.$message({type: data.status, message: data.msg});
                _this.dialogVisible = false;
              }
            })
          }
        })
      }
    },
  }
</script>
<style lang="scss">
  .admin_set{
    width: 60%;
    background-color: #F9FAFC;
    min-height: 400px;
    margin: 20px auto 0;
    border-radius: 10px;
    text-align: left;
    ul{
      list-style: none;
    }
    ul > li{
      padding: 20px;
      span{
        color: #666;
      }
    }
  }
  .admin_title{
    margin-top: 20px;
    font-size: 24px;
    color: #666;
    text-align: center;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    margin-top: 10px;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #20a0ff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
  .avatar {
    width: 120px;
    height: 120px;
    display: block;
  }
</style>
