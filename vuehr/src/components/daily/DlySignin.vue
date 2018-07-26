<template>
  <div>
    <div style="margin-top: 20px;">
      <el-container>
        <el-header style="text-align: left;padding-left: 0px;margin-top: 10px">
          <el-button v-if="isSign == 0" @click="doSign" icon="el-icon-tickets" type="primary" size="mini">签到打卡</el-button>
          <el-button v-if="isSign == 0" @click="doDutySign" icon="el-icon-star-on" type="primary" size="mini">值班</el-button>
          <el-button v-if="isSign == 1" @click="doSignOut" icon="el-icon-tickets" type="danger" size="mini">签退</el-button>
        </el-header>
      </el-container>
    </div>
    <div style="margin-top: 20px">
      <el-table
        v-loading="loading"
        :data="tolist"
        size="mini"
        stripe
        border
        style="width: 70%">
        <el-table-column
          prop="realname"
          label="姓名"
          width="120"
          align="left">
        </el-table-column>
        <el-table-column
          prop="rank"
          label="班次"
          width="80"
          align="left">
          <template slot-scope="scope">
            <div v-if="scope.row.rank==9">
              值班
            </div>
            <div v-else-if="scope.row.rank==1">
              下午
            </div>
            <div v-else>
              上午
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="signtime"
          label="签到时间"
          width="150"
          align="left">
        </el-table-column>
        <el-table-column
          prop="outtime"
          label="签退时间"
          width="150"
          align="left">
        </el-table-column>
        <el-table-column label="操作" align="left"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return {
        isSign: 0,
        iduty: 0,
        flg: 0,
        sign: [],
        tolist: [],
        loading: false,
      }
    },
    methods: {
      checkin(){
        var _this = this;
        _this.getRequest("/daily/punch/getsign", {
          userid: this.user.id
        }).then(resp=> {
          if (resp && resp.status == 200) {
            var data = resp.data;
            this.isSign = 1;
            this.iduty = data.msg.duty;
            this.sign = data.msg;
          }
        })
      },
      doSign() {
        var _this = this;
        const param = {
          iDuty: _this.iduty,
          bz: _this.isSign
        }
        _this.postRequest('/daily/punch/daka', param).then(resp=>{
          if (resp && resp.status == 200) {
            this.isSign = 1;
            this.checkin();
          }
        })
      },
      doSignOut() {
        var _this = this;
        if (_this.sign && _this.sign.id != 0) {
          this.putRequest('/daily/punch/signout', {id: this.sign.id}).then(resp=>{
            if(resp&&resp.status==200) {
              var data = resp.data;
              _this.isSign = 0;
              _this.iduty = 0;
              _this.$message({type: data.status, message: data.msg});
              this.checkin();
            }
          })
        }
      },
      doDutySign() {
        this.iduty = 1;
        this.doSign();
      },
      getTodayList() {
        var _this = this;
        _this.getRequest('/daily/punch/todaylist').then(resp=>{
          if (resp && resp.status == 200) {
            _this.tolist = resp.data;
          }
        })
      }
    },
    mounted:function () {
      this.checkin();
      this.getTodayList();
    },
    computed: {
      user(){
        return this.$store.state.user;
      },
    }
  }
</script>
