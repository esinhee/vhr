<template>
  <div style="text-align: left;">
    <el-row type="flex" justify="start" style="margin-top: 20px;">
      <el-col :span="16" :offset="2">
        <el-form :model="ticketFrm" :rules="rules" ref="ticketForm" label-width="110px" class="demo-ticketFrm" label-position="right">
          <el-form-item label="问题类型" prop="tktype">
            <!--<el-select
              v-model="ticketFrm.tktype" value-key="id" placeholder="请选择">
              <el-option
                v-for="item in ticketTypes"
                :key="item.id"
                :label="item.typename"
                :value="item">
                <span style="float: left">{{ item.typename }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.id }}</span>
              </el-option>
            </el-select>-->
            <el-cascader
              :options="ticketTypes"
              v-model="ticketFrm.tktype"
              :props="ttprops"
              :clearable="true"
              expand-trigger="hover"
              :show-all-levels="false"
              change-on-select
            ></el-cascader>
          </el-form-item>
          <el-form-item label="重要程度" prop="grade">
            <el-radio-group v-model="ticketFrm.grade">
              <el-radio-button label="1">普通</el-radio-button>
              <el-radio-button label="2">重要</el-radio-button>
              <el-radio-button label="3">重大</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="反馈人" prop="reporter">
            <el-select
              v-model="ticketFrm.reporter"
              filterable
              remote
              value-key="sid"
              :remote-method="searchOperator"
              placeholder="输入拼音缩写可以检索反馈人"
              :loading="oploading">
              <el-option
                v-for="item in ops"
                :key="item.sid"
                :label="item.sname"
                :value="item">
              <span style="float: left">{{ item.sname }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.sid }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="反馈科室" prop="dep">
            <!--<el-select
              v-model="ticketFrm.dep"
              filterable
              remote
              value-key="kid"
              placeholder="请输入关键词"
              :loading="ksloading">
              <el-option
                v-for="item in allDeps"
                :key="item.kid"
                :label="item.kname"
                :value="item">
                <span style="float: left">{{ item.kname }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.kid }}</span>
              </el-option>
            </el-select>-->
            <el-cascader
              :options="allDeps"
              v-model="ticketFrm.keshi"
              :props="ksprops"
              :clearable="true"
              expand-trigger="hover"
              :show-all-levels="false"
              change-on-select
            ></el-cascader>
          </el-form-item>
          <el-form-item label="反馈日期" prop="rdate">
            <el-date-picker type="date" placeholder="选择日期" v-model="ticketFrm.rdate" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
          </el-form-item>
          <el-form-item label="问题描述" style="height: 220px;" prop="content">
            <quill-editor
              v-model="ticketFrm.content"
              :options="editorOption"
            >
            </quill-editor>
          </el-form-item>
          <el-form-item label="相关文件" prop="att">
            <template>
              <div class="img-list">
                <div class="img-content" v-for="(item,key) in imagelist" :key="key">
                  <img :src="item.url">
                  <div class="name">
                    <div>{{ item.name }}</div>
                    <el-button type="text" @click="handleFileName(item,key)">修改名字</el-button>
                  </div>
                  <!-- 删除icon -->
                  <div class="del">
                    <i @click="handleFileRemove(item,key)" class="el-icon-delete"></i>
                  </div>
                  <!-- 放大icon -->
                  <div class="layer" @click="handleFileEnlarge(item.url)">
                    <i class="el-icon-view"></i>
                  </div>
                </div>
                <div v-if="!pass && progress !== 0" class="img-content img-progress">
                  <el-progress type="circle" :percentage="progress" :status="proStatus"></el-progress>
                </div>
                <div class="img-upload">
                  <el-upload class="uploader" accept="image/*"
                             ref="upload"
                             list-type="picture-card"
                             :show-file-list="false"
                             action="123"
                             :before-upload="beforeUpload"
                             :on-change="uploadOnChange"
                             :on-success="uploadOnSuccess"
                             :on-error="uploadOnError"
                             :on-progress="uploadOnProgress">
                    <el-button type="primary">点击上传</el-button>
                  </el-upload>
                </div>
                <el-dialog title="" :visible.sync="isEnlargeImage" size="large" :modal-append-to-body="false" top="8%" width="60%">
                  <img @click="isEnlargeImage = false" style="width:100%;" :src="enlargeImage">
                </el-dialog>
              </div>
            </template>
          </el-form-item>
          <el-form-item label="处理方式">
            <el-radio-group v-model="ticketFrm.dowith" @change="changeDealer">
              <el-radio-button label="0" :disabled="dealerflg">工单分配</el-radio-button>
              <el-radio-button label="1">本人处理</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="分配处理" prop="dealer" v-if="ticketFrm.dowith==0">
            <el-select
              v-model="ticketFrm.dealer"
              value-key="id"
              placeholder="请输入关键词">
              <el-option
                v-for="item in czrys"
                :key="item.id"
                :label="item.name"
                :value="item">
                <span style="float: left">{{ item.name }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.id }}</span>
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="addTicket('ticketForm')">保存</el-button>
            <el-button @click="resetForm('ticketForm')">取消</el-button>
          </el-form-item>
        </el-form>
      </el-col>

    </el-row>
  </div>
</template>
<script>
  import Quill from 'quill'
  import {container, ImageExtend, QuillWatch} from 'quill-image-extend-module';
  Quill.register('modules/ImageExtend', ImageExtend);

  export default {
    name: 'upload-list',
    data() {
      var validateInput = (rule, value, callback) => {
        if (this.ticketFrm.reporter == '' &&  this.ticketFrm.keshi.length == 0) {
          callback(new Error('反馈人或者反馈科室至少填一项！'));
        } else {
          callback();
        }
      };
      var checkDealer = (rule, value, callback) => {
        if (this.ticketFrm.dowith == 0 && this.ticketFrm.dealer == '') {
          callback(new Error('分配模式下，任务需指定处理人员！'));
        } else {
          callback();
        }
      };
      return {
        dialogVisible: false,
        tableLoading: false,
        gradeDefault: 1,
        index: 0,
        ksloading: false,
        oploading: false,
        salaries: [],
        loading: false,
        isEnlargeImage: false,//放大图片
        enlargeImage: '',//放大图片地址
        progress: 0,//上传进度
        pass: null,//是否上传成功
        params: {
          action: 'http://jsonplaceholder.typicode.com/posts/',
          data: {}
        },
        multipleSelection: [],
        ticketFrm: {
          grade: 1,
          tktid: '',
          tktname: '',
          tktype: [],
          reportid: '',
          reporter: '',
          keshi: [],
          depid: '',
          dep: '',
          rdate: '',
          content: '',
          dealerid: '',
          dealer: '',
          dowith: 0,
        },
        ticketTypes: [],
        staffs: [],
        ops: [],
        allDeps: [],
        czrys: [],
        iDuty: 0,
        dealerflg: false,
        rules: {
          tktype: [{required: true, message: '请选择问题类型', trigger: 'blur'}],
          rdate: [{required: true, message: '请填写反馈日期', trigger: 'blur'}],
          content: [{required: true, message: '请详细描述问题内容', trigger: 'blur'}],
          reporter: [{ validator: validateInput, trigger: 'change' }],
          dep: [{ validator: validateInput, trigger: 'change' }],
          dealer:  [{validator: checkDealer, trigger: 'blur'}],
        },
        editorOption: {
          modules: {
            ImageExtend: {
              loading: true,
              name: 'img',
              size: 3, //图片大小，单位M
              action: '',
              response: (res) => {
                return res.info
              }
            },
            toolbar: {
              container: [
                [{'header': [1, 2, 3, false]}],
                ['bold', 'italic', 'underline', 'strike'],
                [{'list': 'ordered'}, {'list': 'bullet'}],
                [{'color': []}, {'background': []}],
                ['image', 'code-block']
              ],
              handlers: {
                'image': function () {
                  QuillWatch.emit(this.quill.id)
                }
              }
            }
          },
        },
        imagelist: [],
        ksprops: {
          label: 'kname',
          value: 'kid',
          children: 'children'
        },
        ttprops: {
          label: 'typename',
          value: 'id',
          children: 'children'
        }
      };
    },
    computed: {
      proStatus(){//上传状态
        if(this.pass){
          return 'success'
        }else if(this.pass == false){
          return 'exception'
        }else{
          return ''
        }
      },
      user(){
        return this.$store.state.user;
      },
    },
    methods: {
      beforeUpload(file) {
        console.log(file)
        let fd = new FormData()
        fd.append('file', file, file.name)
        fd.append('cid', 1);
        this.uploadFileRequest("/ticket/fileUpload", fd).then(resp=>{
          if (resp && resp.status == 200) {
            this.$message.success("上传成功")
            //重置progress组件
            this.pass = null;
            this.progress = 0;
            this.imagelist.push({
              id: resp.data.msg.atid,
              url: resp.data.msg.atpath,
              name: resp.data.msg.atname
            });
          }
        })
        return true
      },
      uploadOnProgress(e,file){//开始上传
        console.log(e.percent,file)
        this.progress = Math.floor(e.percent)
      },
      uploadOnChange(file){
        console.log("——————————change——————————")
        console.log(file)
        /*if(file.status == 'ready'){
          console.log("ready")
          //重置progress组件
          this.pass = null;
          this.progress = 0;
        }else if(file.status == 'fail'){
          this.$message.error("图片上传出错，请刷新重试！"+file.status)
        }*/
      },
      uploadOnSuccess(e,file){//上传附件
        console.log("——————————success——————————")
        this.pass = true;
        this.$message.success("上传成功")
        /*this.imagelist.push({
          url: file.url,
          name: '新增图片'
        })
        this.imagelist.push(this.$refs.upload.uploadFiles.msg);*/
      },
      uploadOnError(e,file){
        console.log("——————————error——————————")
        console.log(e)
        this.pass = false;
      },
      handleFileEnlarge(_url){//放大图片
        console.log(_url)
        if(_url){
          this.enlargeImage = _url;
          this.isEnlargeImage = !this.isEnlargeImage;
        }
      },
      handleFileName(file,i){//修改名字
        console.log(file,i)
        let that = this;
        this.$prompt("请输入新文件名：","提示：",{
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(({ value }) => {
          console.log(value)
          if(!value){
            return false;
          }
          //可添加ajax
          this.$message.success("操作成功")
          that.imagelist[i].name = value
        }).catch(() => {})
      },
      handleFileRemove(file,i){//删除图片
        console.log(file,i)
        if(!file.url){
          return false;
        }
        let that = this;
        this.$confirm('是否删除此附件？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //可添加ajax
          this.$message.success("删除成功")
          this.$message({
            type: 'success',
            message: '删除成功',
            onClose: () => {
              that.imagelist.splice(i,1)
            }
          })
        }).catch((meg) => console.log(meg))
      },
      getTicketTypeList() {
        var _this = this;
        this.getRequest("/system/config/tttoplist/0").then(resp=> {
          if (resp && resp.status == 200) {
            _this.ticketTypes = resp.data;
            if (!_this.ticketTypes) return;
            _this.ticketTypes.forEach(function (res) {
              _this.traverseTree(res)
            })
          }
        });
      },
      getStaffList() {
        var _this = this;
        this.getRequest("/ticket/stafflist").then(resp=> {
          if (resp && resp.status == 200) {
            _this.staffs = resp.data;
          }
        });
      },
      searchOperator(q) {  //反馈人匹配，根据输入的拼音缩写检索
        if (q !== '') {
          this.oploading = true;
          setTimeout(()=> {
           this.oploading = false;
           this.ops = this.staffs.filter(item => {
             return item.py.toLowerCase().indexOf(q.toLowerCase()) > -1;
           });
          }, 200);
        } else {
          this.ops = [];
        }
      },
      loadAllDeps(){
        var _this = this;
        this.getRequest("/system/config/ksplist/0").then(resp=> {
          if (resp && resp.status == 200) {
            this.allDeps = resp.data;
            if (!this.allDeps) return;
            this.allDeps.forEach(function (r) {
              _this.traverseTree(r)
            })
          }
        });
      },
      getCzryList() {
        var _this = this;
        this.getRequest("/ticket/oplist").then(resp=> {
          if (resp && resp.status == 200) {
            _this.czrys = resp.data;
          }
        });
      },
      addTicket(frm) {
        var _this = this;
        this.$refs[frm].validate((valid) => {
          if (valid) {
            if (this.ticketFrm.dowith == 1) { //本人处理模式，设置当前处理人
              console.log(this.user.id);
              //this.ticketFrm.dealer = this.user.id;
            }
            _this.changeKS(_this.ticketFrm.keshi);
            _this.changeTT(_this.ticketFrm.tktype);
            const tkparams = {
              etid: this.ticketFrm.tktid,
              etname: this.ticketFrm.tktname,
              grade: this.ticketFrm.grade,
              kid: this.ticketFrm.depid,
              kname: this.ticketFrm.dep,
              reportid: this.ticketFrm.reporter===''? '':this.ticketFrm.reporter.sid,
              reporter: this.ticketFrm.reporter===''?'':this.ticketFrm.reporter.sname,
              reporttime: this.ticketFrm.rdate,
              content: this.ticketFrm.content,
              bz: 1,
              dealflg: 1,
              dealerid: this.ticketFrm.dealer===''?this.user.id:this.ticketFrm.dealer.id,
              dealer: this.ticketFrm.dealer===''?this.user.name:this.ticketFrm.dealer.name,
            };
            _this.postRequest("/ticket/add", tkparams).then(resp=> {
              if (resp && resp.status == 200) {
                var data = resp.data;
                var atList = [];
                //_this.$message({type: data.status, message: data.msg});
                if (_this.imagelist.length > 0) {
                  _this.imagelist.forEach(imgitem=>{
                    atList.push(imgitem.id)  //附件id列表
                  })
                  let params = {
                    eid: data.msg,
                    atList: atList
                  }
                  _this.postRequest("/ticket/releated", params).then(res=> {
                    if (res && res.status == 200) {
                      _this.$message.success("操作成功！");
                      _this.resetForm(frm);
                      this.$router.push({path: '/ticket/list'});
                    } else {
                      _this.$message.error("附件操作失败！");
                    }
                  })
                } else {
                  _this.$message.success("操作成功！");
                  _this.resetForm(frm);
                  this.$router.push({path: '/ticket/list'});
                }
              }
            });
          } else {
            this.$notify.error({
              title: '错误',
              message: '请检查输入是否正确',
              offset: 100
            });
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      checkDuty() {
        var _this = this;
        _this.getRequest('/daily/punch/checkduty/'+ _this.user.id).then(resp=>{
          if (resp && resp.status == 200) {
            if (resp.data == 0) { //非值班人员
              _this.iDuty = 1;
              _this.ticketFrm.dowith = 1;
              _this.dealerflg = true;
            }
          }
        })
      },
      changeDealer() {
        this.ticketFrm.dealer = '';
      },
      traverseTree(node) {
        if (!node) return;
        if (node.children && node.children.length > 0) {
          var i = 0;
          for (i = 0; i < node.children.length; i++) {
            this.traverseTree(node.children[i]);
          }
        } else {
          node.children = null;
        }
      },
      changeKS(val) {
        var _this = this;
        var len = val.length;
        if (len !== 0) {
          var sel = val[len-1];
          this.allDeps.forEach(function (res) {
            var _stack = [];
            _stack.unshift(res);
            while (_stack.length > 0) {
              let tmp = _stack.shift();
              if (sel == tmp.kid) {
                _this.ticketFrm.depid = sel;
                _this.ticketFrm.dep = tmp.kname;
                return;
              }
              if (tmp.children && tmp.children.length > 0) {
                _stack = tmp.children.concat(_stack);
              }
            }
          })
        }
      },
      changeTT(val) {
        var _this = this;
        var len = val.length;
        if (len !== 0) {
          var sel = val[len-1];
          this.ticketTypes.forEach(function (res) {
            var _stack = [];
            _stack.unshift(res);
            while (_stack.length > 0) {
              let tmp = _stack.shift();
              if (sel == tmp.id) {
                _this.ticketFrm.tktid = sel;
                _this.ticketFrm.tktname = tmp.typename;
                return;
              }
              if (tmp.children && tmp.children.length > 0) {
                _stack = tmp.children.concat(_stack);
              }
            }
          })
        }
      }
    },
    mounted: function () {
      this.checkDuty();
      this.getTicketTypeList();
      this.getStaffList();
      this.loadAllDeps();
      this.getCzryList();
    }
  };
</script>
<style>
  *{
    box-sizing: border-box;
  }
  .img-list{
    overflow:hidden;
    width:100%;
  }
  .img-list .img-content{
    float:left;
    position:relative;
    display:inline-block;
    width:150px;
    height:200px;
    padding:5px;
    margin:5px 20px 20px 0;
    border:1px solid #d1dbe5;
    border-radius:4px;
    transition:all .3s;
    box-shadow:0 2px 4px 0 rgba(0,0,0,.12), 0 0 6px 0 rgba(0,0,0,.04);
  }
  .img-list .img-content img{
    display:block;
    width:100%;
    height:140px;
    margin:0 auto;
    border-radius:4px;
  }
  .img-list .img-content .name{
    margin-top:3px;
  }
  .img-list .img-content .name>div{
    width:90%;
    text-overflow:ellipsis;
    overflow:hidden;
    height:18px;
    line-height:18px;
  }
  .img-list .img-content:hover .del,
  .img-list .img-content:hover .layer{
    opacity:1;
  }
  .img-list .img-content .del,
  .img-list .img-content .layer{
    opacity:0;
    transition:all .3s;
  }
  .img-list .img-content .del{
    position:absolute;
    bottom:1px;
    right:10px;
    color:#8492a6;
    cursor:pointer;
    font-size:1.1em;
  }
  .img-list .img-content .layer{
    position:absolute;
    left:0;
    right:0;
    top:0;
    height:150px;
    color:#fff;
    text-align:center;
    z-index:5;
    background-color:rgba(0,0,0,.4);
  }
  .img-list .img-content .layer i{
    font-size:1.6em;
    margin-top:65px;
  }
  .img-list .img-upload{
    float:left;
    width:150px;
    height:200px;
    display:table;
    text-align:center;
  }
  .img-list .uploader{
    width:100%;
    display:table-cell;
    vertical-align:middle;
  }
  .img-list .img-progress{
    text-align:center;
    padding-top:50px;
  }
  .el-dialog__body {
    padding-bottom: 10px;
  }
  .quill-editor {
    line-height: 25px;
    height: 170px;
  }
  /* 主内容区 */
  .mainbox{display: -webkit-box;display: -ms-flexbox;display: flex;  min-height: 800px; margin-top: 20px; border: solid 30px #E9ECF1;  background-color: #FCFCFC;  }
  .quill-editor {
    /*工具栏内用*/
    .ql-font {
      span[data-value="Arial"]::before {
        content: "Arial" !important;
        font-family: "Arial";
      }
      span[data-value="宋体"]::before {
        content: "宋体" !important;
        font-family: "宋体";
      }
      span[data-value="黑体"]::before {
        content: "黑体" !important;
        font-family: "黑体";
      }
      span[data-value="微软雅黑"]::before {
        content: "微软雅黑" !important;
        font-family: "微软雅黑";
      }
    }
    /*编辑器内容用*/
    .ql-font-Arial {
      font-family: "Arial";
    }
    .ql-font-宋体 {
      font-family: "宋体";
    }
    .ql-font-黑体 {
      font-family: "黑体";
    }
    .ql-font-微软雅黑 {
      font-family: "微软雅黑";
    }
  }
  .avatar-uploader {
    display: none;
  }
</style>
