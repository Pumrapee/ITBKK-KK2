<script setup>
import { ref } from "vue"
import { useModalStore } from "../stores/modal"
import { defineProps, defineEmits } from "vue"
import { useTaskStore } from "@/stores/taskStore";
import { deleteItemById } from "../libs/fetchUtils"

const modal = useModalStore()
const mytasks = useTaskStore()
const task = ref(mytasks.getTasks())

console.log(mytasks.getTasks())

console.log(modal.deleteId)

const { showDelete } = defineProps({
  showDelete: Boolean,
})

const emits = defineEmits(["closeDeleteModal"])

const confirmDelete = async () => {
  const deleteItem = await deleteItemById(import.meta.env.VITE_BASE_URL, modal.deleteId)
  
    if(deleteItem === 200) {
      mytasks.removeTodo(modal.deleteId)
    }
  
  // ปิด Modal
  modal.showDelete= false

  // ทำการลบข้อมูล
}

const cancelDelete = () => {
  // ปิด Modal
  modal.showDelete= false

}
</script>

<template>
    <div v-if="modal.showDelete" class="fixed z-10 inset-0 overflow-y-auto">
    <div class="flex items-center justify-center min-h-screen bg-black/[.15]">
      <div
        class="grid grid-rows-2 grid-cols-2 bg-white p-10 rounded-lg w-96"
      >
       

        <div class="col-span-full text-center">
          <p class="text-lg font-semibold mb-4">Are you sure you want to delete 
          <span class="text-blue-400">"{{ modal.deleteTitle }}"</span> <span>?</span> </p>
        </div>

        <div class="col-span-4 place-self-end rounded-lg ">
          <button class="btn mr-4 bg-red-500 text-white" @click="confirmDelete()">
            Delete
          </button>
          <button class="btn" @click="cancelDelete()">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
  
<style scoped>

</style>
