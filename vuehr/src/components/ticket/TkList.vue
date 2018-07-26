<template>
  <div style="margin-top: 10px">
    <el-tabs v-model="defaultTab" type="card">
      <el-tab-pane label="今日工单" name="tlToday">
        <tl-today></tl-today>
      </el-tab-pane>
      <el-tab-pane label="个人未完成工单" name="tlUndo">
        <tl-undo></tl-undo>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import TlToday from './list/TLToday.vue'
  import TlUndo from './list/TLUndo'
  export default {
    data() {
      return {
        defaultTab: 'tlToday'
      };
    },
    methods: {},
    components: {
      'tl-today': TlToday,
      'tl-undo' : TlUndo,
    }
  };
</script>
