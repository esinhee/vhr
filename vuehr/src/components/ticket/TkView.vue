<template>
  <div style="text-align: left;margin-top: 20px" v-loading="loading">
    <div>
      <el-row :gutter="40">
        <el-col :span="21">
          <el-steps :active="process" finish-status="success" simple>
            <el-step title="登记" icon="el-icon-service"></el-step>
            <el-step title="分配" icon="el-icon-question"></el-step>
            <el-step title="处理" icon="el-icon-loading"></el-step>
            <el-step title="完成" icon="el-icon-success"></el-step>
          </el-steps>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" icon="el-icon-back" @click="$router.back(-1)">返回</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="info">
      <el-row :gutter="12">
        <el-col :span="12">
          <el-card :body-style="{ padding: '0px' }" class="box-card">
            <div slot="header" class="clearfix">工单详情</div>
            <div class="item">
              <ul>
                <li>
                  <span>工单 ID：</span><span>{{tk.eid}}</span>
                </li>
                <li>
                  <span>问题等级：</span><span>{{grade}}</span>
                </li>
                <li>
                  <span>反 馈 人：</span><span>{{tk.reporter}}</span>
                </li>
                <li>
                  <span>反馈科室：</span><span>{{tk.kname}}</span>
                </li>
                <li>
                  <span>反馈日期：</span><span>{{tk.reporttime}}</span>
                </li>
                <li>
                  <span>记 录 人：</span><span>{{tk.recorder}}</span>
                </li>
                <li>
                  <span>记录时间：</span><span>{{tk.recordetime}}</span>
                </li>
                <li>
                  <span>问题描述：</span><span v-html="tk.content"></span>
                </li>
              </ul>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">工单处理</div>
            <div class="tplist">
              <ol>
                <li v-for="tp in tplist" :key="tp.id">
                  <span>{{tp.opname}}</span>
                  <p v-html="tp.content"></p>
                </li>
              </ol>
            </div>
            <div class="item" v-if="showbtn()">
              <div>
                <quill-editor
                  v-model="tkpro"
                  :options="editorOption"
                >
                </quill-editor>
              </div>
              <div style="margin-top: 50px">
                <el-button type="primary" @click="save(1)">保存处理</el-button>
                <el-button type="success" @click="save(2)" v-if="tk.dealflg < 7 ">完成任务</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
  import Quill from 'quill'
  import {container, ImageExtend, QuillWatch} from 'quill-image-extend-module';
  Quill.register('modules/ImageExtend', ImageExtend);

  export default {
    data(){
      return {
        eid: '',
        loading: false,
        tk: [],
        grade: '',
        process: 0,
        tkpro: '',
        tplist: [],
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
      }
    },
    mounted: function () {
      this.loading = true;
      this.loadTicketInfo();
      this.getProcess();
    },
    methods: {
      loadTicketInfo() {
        var _this = this;
        _this.eid = _this.$route.query.id;
        this.loading = true;
        if (_this.eid != '') {
          _this.getRequest("/ticket/tkinfo/" + _this.eid).then(resp=>{
            this.loading = false;
            if (resp && resp.status == 200) {
              _this.tk = resp.data.msg;
              this.initData();
            }
          })
        }
      },
      initData() {
        var _this = this;
        switch (_this.tk.grade) {
          case 1:
            _this.grade = '普通';
            break;
          case 2:
            _this.grade = '重要';
            break;
          case 3:
            _this.grade = '重大';
            break;
        }
        if (_this.tk.dealflg == 1 || _this.tk.dealflg == 10) {
          _this.process = 0;
        } else if(_this.tk.dealflg == 2 || _this.tk.dealflg == 5) {
          _this.process = 1;
        } else if(_this.tk.dealflg == 3 || _this.tk.dealflg == 6) {
          _this.process = 2;
        } else if(_this.tk.dealflg == 7 || _this.tk.dealflg == 9) {
          _this.process = 3;
        }
      },
      save(flg) {
        var _this = this;
        if (_this.tkpro == '') {
          _this.$message.error("内容不能为空！");
          return
        }
        const param = {
          eid: _this.eid,
          content: _this.tkpro,
        };
        this.loading = true;
        if (flg == 1 ) {
          _this.postRequest('/ticket/newep', param).then(resp=>{
            this.loading = false;
            _this.tkpro = '';
            if (resp && resp.status == 200) {
              _this.getProcess();
              _this.loadTicketInfo();
              _this.$message.success("操作成功！");
            } else {
              _this.$message.error("操作失败！");
            }
          })
        } else if (flg == 2) {
          _this.postRequest('/ticket/newepc', param).then(resp=>{
            this.loading = false;
            _this.tkpro = '';
            if (resp && resp.status == 200) {
              _this.getProcess();
              _this.loadTicketInfo();
              _this.$message.success("操作成功！");
            } else {
              _this.$message.error("操作失败！");
            }
          })
        }
      },
      getProcess() {
        var _this = this;
        this.loading = true;
        _this.getRequest('/ticket/tp/'+_this.eid).then(resp=>{
          this.loading = false;
          if (resp && resp.status == 200) {
            _this.tplist = resp.data;
          }
        })
      },
      showbtn() {
        if (this.user.id == this.tk.dealerid) {
          return true;
        } else {
          return false;
        }
      }
    },
    computed: {
      user(){
        return this.$store.state.user;
      },
    }
  }
</script>
<style lang="scss">
  .info {
    width: 100%;
    min-height: 400px;
    margin: 20px auto 0;
    border-radius: 10px;
    text-align: left;
    ul{
      list-style: none;
    }
    ul > li{
      padding: 10px;
      span{
        color: #666;
      }
    }
    .clearfix:before,
    .clearfix:after {
      display: table;
      content: "";
    }
    .clearfix:after {
      clear: both
    }
    .item {
      margin-bottom: 18px;
    }
    .box-card {
      //width: 520px;
    }
  }
  .tplist {
    margin-top: 10px;
    ol {
      color: #ccc;
      list-style-type: none;
      padding-left: 10px;
    }
    ol > li {
      position: relative;
      margin-bottom: 20px;
      p {
        padding-left: 80px;
        color: #555;
      }
      span {
        position: absolute;
      }
    }
  }
</style>
