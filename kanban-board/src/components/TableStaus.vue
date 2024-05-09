<script setup>
import { ref } from "vue"
import { useTaskStore } from "../stores/taskStore"
import { useStatusStore } from "../stores/statusStore"
// import dataStatus from "../../data/task.json"
import AddStatus from "./AddStatus.vue"
import EditStatus from "./EditStatus.vue"
import router from "@/router"
import { getItemById } from "../libs/fetchUtils"

const myTask = useTaskStore()
const myStatus = useStatusStore()
const showAddModal = ref(false)
const showEditModal = ref(false)
const statusItems = ref()
myTask.showNavbar = false

console.log(myStatus.getStatus())
const openAddStatus = () => {
  showAddModal.value = true
}

const closeModal = () => {
  showAddModal.value = false
  showEditModal.value = false
  router.go(-1)
}

const openEditStatus = async (idStatus) => {
  if (idStatus) {
    const statusItem = await getItemById(
      `${import.meta.env.VITE_BASE_URL}statuses`,
      idStatus
    )
    statusItems.value = statusItem
    showEditModal.value = true
    console.log(statusItem)
  }
}
</script>

<template>
  <div class="flex flex-col items-center mt-16 mb-20">
    <!-- Navigation -->
    <div class="flex justify-between w-4/5">
      <div class="flex text-sm breadcrumbs text-blue-400">
        <ul>
          <li><RouterLink :to="{ name: 'task' }"> Home</RouterLink></li>
          <li>Task Status</li>
        </ul>
      </div>
      <RouterLink :to="{ name: 'AddStatus' }">
        <button @click="openAddStatus" class="btn">Add status</button>
      </RouterLink>
    </div>

    <!-- Task Table -->
    <div class="overflow-x-auto border border-blue-400 rounded-md w-4/5 mt-4">
      <table class="table">
        <thead class="bg-blue-400">
          <tr class="text-white text-sm">
            <th></th>
            <th class="pl-20">Name</th>
            <th class="pl-20">Description</th>
            <th class="pl-20">Action</th>
          </tr>
        </thead>
        <tbody class="bg-white">
          <tr
            v-for="(task, index) in myStatus.getStatus()"
            :key="task.id"
            class="itbkk-item"
          >
            <th class="text-blue-400 pl-20">{{ index + 1 }}</th>

            <td class="pl-20 w-1/3">
              <p class="h-5 font-medium" style="text-align: left">
                {{ task.name }}
              </p>
            </td>

            <td
              class="itbkk-assignees pl-20"
              :style="{ 'font-style': task.description ? 'medium' : 'italic' }"
            >
              <p v-if="task.description">
                {{ task.description }}
              </p>
              <p v-else class="text-gray-500 font-medium">Unassigned</p>
            </td>

            <!-- ใส่ v-if เพื่อตรวจสอบว่า index เท่ากับ 0 หรือไม่ -->
            <td v-if="index !== 0" class="ml-10 flex">
              <div class="mr-2">
                <router-link
                  :to="{ name: 'EditStatus', params: { id: task.id } }"
                >
                  <button
                    @click="openEditStatus(task.id)"
                    class="btn btn-ghost h-auto bg-yellow-100"
                  >
                    <img src="/icons/pen.png" class="w-4" />
                  </button>
                </router-link>
              </div>
              <div>
                <button class="itbkk-button-delete btn bg-red-500">
                  <img src="/icons/delete.png" class="w-4" />
                </button>
              </div>
            </td>
            <td v-else="index === 0" class="h-20"></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <EditStatus
    :showEditStatus="showEditModal"
    @closeEditStatus="closeModal"
    :taskStatus="statusItems"
  />
  <AddStatus :showAddStatus="showAddModal" @closeAddStatus="closeModal" />
</template>

<style scoped></style>
