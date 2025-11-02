<template>
  <div class="card text-white h-100" :class="cardClass">
    <div class="card-body">
      <div class="d-flex justify-content-between">
        <div>
          <h4 class="mb-1">{{ value }}</h4>
          <p class="mb-0">{{ title }}</p>
        </div>
        <div class="align-self-center">
          <i :class="iconClass + ' fa-2x'"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  value: [String, Number],
  iconClass: String,
  cardClass: String
})
</script>