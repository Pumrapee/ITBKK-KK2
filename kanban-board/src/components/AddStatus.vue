<script setup>
import { defineProps, defineEmits, ref, computed } from "vue"
import { useStatusStore } from "@/stores/statusStore"
import { addItem } from "@/libs/fetchUtils"

const { showAddStatus } = defineProps({
  showAddStatus: Boolean,
})

const emits = defineEmits(["closeAddStatus", "closeCancleStatus"])

const newStatus = ref({
  name: "",
  description: "",
})

const errorStatus = ref({
  name: "",
  description: "",
})
const color = ref("")
console.log(color)

const myStatus = useStatusStore()
const saveStatus = async () => {
  // Unique
  // const oldStatus = myStatus.getStatus()
  // const checkUnique = newStatus.value.name === oldStatus.name
  // console.log(checkUnique)

  // Trim
  newStatus.value.name = newStatus.value.name?.trim()
  newStatus.value.description = newStatus.value.description?.trim()

  // Replace empty strings with null
  if (newStatus.value.name === "") {
    newStatus.value.name = null
  }
  if (newStatus.value.description === "") {
    newStatus.value.description = null
  }

  const { newTask, statusCode } = await addItem(
    `${import.meta.env.VITE_BASE_URL}statuses`,
    newStatus.value
  )

  if (statusCode === 201) {
    myStatus.addOneStatus(newTask.id, newTask.name, newTask.description)

    newStatus.value.name = ""
    newStatus.value.description = ""
    emits("closeAddStatus", statusCode)
  }
  if (statusCode === 400 || statusCode === 500) {
    emits("closeAddStatus", statusCode)
  }
}

const cancleStatus = () => {
  newStatus.value.name = ""
  newStatus.value.description = ""
  emits("closeCancleStatus")
}

const changeStatus = computed(() => {
  const trimmedNameLength = newStatus.value.name?.trim()?.length
  if (trimmedNameLength > 50 || trimmedNameLength === 0) {
    errorStatus.value.name =
      trimmedNameLength === 0
        ? "Name is required."
        : "Name exceeds the limit of 50 characters."
    return true
  } else {
    errorStatus.value.name = ""
  }

  //ยังไม่ได้ ต้องพิม name ก่อนถึงจะขึ้น
  newStatus.value.description?.trim()?.length > 200
    ? (errorStatus.value.description =
        "Description exceeds the limit of 200 characters.")
    : (errorStatus.value.description = "")
  console.log(errorStatus.value.description)
  return !newStatus.value.name
})
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
          v-model="newStatus.name"
          id="name"
          class="w-full border border-blue-400 rounded-lg py-2 px-3 input input-ghost"
        />
        <div class="flex justify-between items-center">
          <p class="text-red-400">
            {{ errorStatus.name }}
          </p>
          <p
            class="text-gray-300 pb-4 text-sm"
            :class="{
              'text-red-400': newStatus.name?.trim()?.length > 50,
            }"
          >
            {{ newStatus.name?.trim()?.length }}/50
          </p>
        </div>
      </div>

      <div>
        <label for="description" class="block text-blue-400 font-bold mb-2"
          >Description</label
        >
        <textarea
          id="description"
          v-model="newStatus.description"
          class="w-full border border-blue-400 rounded-lg py-3 px-3 h-44 textarea textarea-ghost"
        ></textarea>
        <div class="flex justify-between items-center">
          <p class="text-red-400">{{ errorStatus.description }}</p>
          <p
            class="text-gray-300 self-end text-sm pb-3"
            :class="{
              'text-red-400': newStatus.description?.trim()?.length > 200,
            }"
          >
            {{ newStatus.description?.trim()?.length }}/200
          </p>
        </div>
      </div>

      <!-- <div class="mb-6 flex">
        <label for="color" class="block text-blue-400 font-bold mb-2"
          >Color:</label
        >
        <input v-model="color" class="ml-3" type="color" />
      </div> -->

      <div class="flex justify-end">
        <button
          @click="saveStatus"
          class="btn bg-green-400 text-white rounded-lg py-2 px-4 mr-2 disabled:bg-green-200"
          :disabled="changeStatus"
        >
          Save
        </button>
        <button
          @click="cancleStatus"
          class="btn bg-gray-300 text-gray-700 rounded-lg py-2 px-4"
        >
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
