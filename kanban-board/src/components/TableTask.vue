<script setup>
import { ref, onMounted } from "vue"
import AddEditTask from "../components/AddEditTask.vue"
import { getItems, getItemById } from "../libs/fetchUtils"
import router from "@/router"

const showModal = ref(false)
const taskdata = ref([])
const task = ref()

const closeModal = () => {
  showModal.value = false
}

const openModal = async (taskId) => {
  console.log(taskId)
  if (taskId) {
    const data = await getItemById(import.meta.env.VITE_BASE_URL, taskId)
    if (data.status === 404) {
      alert("The requested task does not exist")
      //router.go(-1) // Redirect to previous page
      router.push("/task")
    } else {
      task.value = data
      showModal.value = true
    }
  }

  console.log(task.value)
}

onMounted(async () => {
  const data = await getItems(import.meta.env.VITE_BASE_URL)
  taskdata.value = data

  console.log(taskdata.value)

  const format = () => {}
})

const reformat = (status) => {
  const statusMap = {
    NO_STATUS: "No Status",
    TO_DO: "To Do",
    DOING: "Doing",
    DONE: "Done",
  }
  return statusMap[status] || status // ถ้าไม่มีค่าใน statusMap ให้ใช้ค่าเดิม
}
</script>

<template>
  <!-- Task table -->
  <div class="flex justify-center mt-40">
    <div class="overflow-x-auto border-2 rounded-md w-4/5">
      <table class="table">
        <thead class="bg-slate-100">
          <tr>
            <th></th>
            <th>Title</th>
            <th>Assignees</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(task, index) in taskdata" :key="task.id">
            <th>{{ task.id }}</th>
            <td>
              <button @click="openModal(task.id)" class="btn btn-ghost">
                {{ task.title }}
              </button>
            </td>
            <td>
              <p>
                {{ task.assignees }}
              </p>
            </td>
            <td>{{ reformat(task.status) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <AddEditTask @closeModal="closeModal" :showModal="showModal" :task="task" />
</template>

<style scoped></style>
