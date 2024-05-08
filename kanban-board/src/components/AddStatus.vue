<script setup>
import { defineProps, defineEmits } from "vue"

const { showAddStatus } = defineProps({
  showAddStatus: Boolean,
})

defineEmits(["closeAddStatus"])
</script>

<template>
  <!-- Modal window -->
  <div
    v-if="showAddStatus"
    class="fixed z-10 inset-0 overflow-y-auto flex items-center justify-center bg-black/[.15]"
  >
    <div class="bg-white p-6 rounded-lg w-11/12 max-w-xl">
      <h2 class="text-2xl font-bold text-blue-400 mb-4 border-b-2">
        Add Status
      </h2>

      <div class="mb-4">
        <label for="name" class="block text-blue-400 font-bold mb-2"
          >Name</label
        >
        <input
          type="text"
          id="name"
          class="w-full border border-blue-400 rounded-lg py-2 px-3"
        />
      </div>

      <div class="mb-6">
        <label for="description" class="block text-blue-400 font-bold mb-2"
          >Description</label
        >
        <textarea
          id="description"
          class="w-full border border-blue-400 rounded-lg py-3 px-3 h-44"
        ></textarea>
      </div>

      <div class="mb-6 flex">
        <label for="color" class="block text-blue-400 font-bold mb-2"
          >Color:</label
        >
        <input class="ml-3" type="color" />
      </div>

      <div class="flex justify-end">
        <button class="bg-green-400 text-white rounded-lg py-2 px-4 mr-2">
          Save
        </button>
        <button
          @click="$emit('closeAddStatus')"
          class="bg-gray-300 text-gray-700 rounded-lg py-2 px-4"
        >
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
