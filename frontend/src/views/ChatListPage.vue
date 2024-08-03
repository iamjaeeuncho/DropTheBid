<template>
  <div>
    <div class="maintitle aligncenter">채팅방 리스트</div>
    <ul v-if="chatRooms.length">
      <li v-for="chatRoom in chatRooms" :key="chatRoom.chatRoomId">
        <div>Auction ID: {{ chatRoom.auctionId }}</div>
        <div>Member ID: {{ chatRoom.memberId }}</div>
        <button @click="goToChatRoom(chatRoom.chatRoomId)">Go to Chat Room</button>
      </li>
    </ul>
    <p v-else>채팅방이 없습니다.</p>
  </div>
</template>

<script>
import axiosInstance from '../utils/axiosinstance'; // Adjust the import path if necessary

export default {
  data() {
    return {
      chatRooms: [] // Array to hold the chat rooms
    };
  },
  async created() {
    await this.fetchChatRooms();
  },
  methods: {
    async fetchChatRooms() {
      try {
        const response = await axiosInstance.get('http://localhost:8080/find'); // Adjust the endpoint as needed
        this.chatRooms = response.data;
      } catch (error) {
        console.error('Error fetching chat rooms:', error.response?.data || error);
      }
    },
    goToChatRoom(chatRoomId) {
      // Navigate to the chat room page using Vue Router
      this.$router.push(`/chat/${chatRoomId}`);
    }
  }
};
</script>

<style scoped>
@import '@/assets/style.css'; /* Importing global styles */

/* Additional scoped styles */
.maintitle {
  font-size: 24px;
  margin-bottom: 20px;
}

.aligncenter {
  text-align: center;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
