import { ref } from "vue"
import { defineStore, acceptHMRUpdate } from "pinia"

export const useStatusStore = defineStore("status", () => {
  const listStatus = ref([])

  const getStatus = () => {
    return listStatus.value
  }
  //actions
  const addStatus = (newTasks) => {
    newTasks.forEach((newTask) =>
      addOneStatus(newTask.id, newTask.name, newTask.description)
    )
  }
  const addOneStatus = (id, name, desc) => {
    listStatus.value.push({
      id: id,
      name: name,
      description: desc,
    })
  }

  const updateStatus = (id, name, desc) => {
    listStatus.value = listStatus.value.map((todo) => {
      return todo.id === id
        ? {
            ...todo,
            name: name,
            description: desc,
          }
        : todo
    })
  }

  const removeStatus = (removeId) => {
    listStatus.value.splice(
      listStatus.value.findIndex((todo) => todo.id === removeId),
      1
    )
  }
  return { getStatus, addStatus, addOneStatus, removeStatus, updateStatus }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useStatusStore, import.meta.hot))
}
