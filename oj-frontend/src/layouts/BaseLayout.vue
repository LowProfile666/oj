<template>
  <div id="baseLayout">
    <a-layout style="height: 400px">
      <a-layout-header>
        <NavbarMenu />
      </a-layout-header>
      <a-layout-content>
        <router-view />
      </a-layout-content>
      <a-layout-footer>
        <FootBar />
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script lang="ts" setup>
import NavbarMenu from "@/components/NavbarMenu.vue";
import FootBar from "@/components/FootBar.vue";
</script>

<style scoped></style>
