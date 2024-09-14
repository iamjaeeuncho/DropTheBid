<template>
  <div>
    <div v-for="msg in messages" :key="msg.chatMessageId">
      <strong>{{ msg.memberId }}:</strong> {{ msg.message }}
    </div>
    <input v-model="message" @keyup.enter="sendMessage" placeholder="Type your message" />
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axiosInstance from '../utils/axiosinstance';

const loadMemberInfo = async () => {
    try {
        const res = await axiosInstance.get('/members/info');
        return res.data;
    } catch (error) {
        console.error('Failed to load member info:', error);
        throw error;
    }
};

export default {
  data() {
    return {
      message: '',
      messages: [],
      stompClient: null,
      chatRoomId: this.$route.params.id,
      memberId: null, // Initialize memberId
    };
  },
  async mounted() {
    try {
      await this.loadInfo(); // Load member info on mount
      this.connect();
    } catch (error) {
      console.error('Error during mounting:', error);
    }
  },
  beforeDestroy() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  },
  methods: {
    async loadInfo() {
      try {
        const memberInfo = await loadMemberInfo();
        this.memberId = memberInfo.memberId; // Set memberId from response
      } catch (error) {
        console.error('Failed to load member info:', error);
      }
    },
    connect() {
      const socket = new SockJS('http://localhost:8080/ws');
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        onConnect: frame => {
          console.log('Connected: ' + frame);
          this.stompClient.subscribe('/topic/messages', message => {
            this.messages.push(JSON.parse(message.body));
          });
        }
      });
      this.stompClient.activate();
    },
    sendMessage() {
      if (this.message.trim()) {
        const chatMessage = {
          chatMessageId: null,
          chatRoomId: this.chatRoomId,
          memberId: this.memberId,
          message: this.message,
          createdAt: new Date().toISOString()
        };
        this.stompClient.publish({
          destination: '/app/send',
          body: JSON.stringify(chatMessage)
        });
        this.message = '';
      }
    }
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>
