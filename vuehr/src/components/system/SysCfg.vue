<template>
  <div style="margin-top: 10px">
    <el-tabs v-model="defaultTab" type="card">
      <el-tab-pane label="科室管理" name="ksMana">
        <ks-mana></ks-mana>
      </el-tab-pane>
      <el-tab-pane label="工单类型" name="ktTypMana">
        <tkttyp-mana></tkttyp-mana>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import KsMana from './config/KsMana.vue'
  import TktTypMana from './config/TicketTypeMana.vue'
  export default {
    data() {
      return {
        defaultTab: 'ksMana'
      };
    },
    methods: {},
    components: {
      'ks-mana': KsMana,
      'tkttyp-mana': TktTypMana
    }
  };
</script>
