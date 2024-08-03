<template>
  <div>
    <h1>Create Chat Room</h1>
    <form @submit.prevent="createChatRoom">
      <div>
        <label for="auctionId">Auction ID:</label>
        <input v-model="auctionId" type="number" id="auctionId" required />
      </div>
      <button type="submit">Create Chat Room</button>
    </form>
    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script>
import axiosInstance from '../utils/axiosinstance.js';

export default {
  data() {
    return {
      auctionId: '',
      message: ''
    };
  },
  methods: {
    async createChatRoom() {
      const auctionId = Number(this.auctionId);

      try {
        const response = await axiosInstance.post('http://localhost:8080/chat', {
            auctionId: auctionId,
        });
        
        const chatRoomId = response.data;
        
        if (chatRoomId) {
          this.$router.push(`/chat/${chatRoomId}`);
        } else {
          this.message = 'Chat room created successfully but no ID was returned';
        }
      } catch (error) {
        console.error('Error creating chat room:', error.response?.data || error);
        this.message = error.response?.data || 'Error creating chat room.';
      }
    }
  }
};
</script>

<style scoped>

</style>