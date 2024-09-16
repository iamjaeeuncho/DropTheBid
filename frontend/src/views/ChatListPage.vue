<template>
  <div>
    <div class="maintitle aligncenter">채팅방 리스트</div>
    <ul v-if="chatRooms.length">
      <li v-for="chatRoom in chatRooms" :key="chatRoom.chatRoomId">
        <div>Auction ID: {{ chatRoom.auctionId }}</div>
        <div>Member ID: {{ chatRoom.memberId }}</div>
        <button @click="goToChatRoom(chatRoom.auctionId, chatRoom.memberId)">채팅방 가기</button>
      </li>
    </ul>
    <p v-else>채팅방이 없습니다.</p>
  </div>
</template>

<script>
import axiosInstance from '../utils/axiosinstance';

// 회원 정보 로드
const loadMemberInfo = async () => {
  try {
    const res = await axiosInstance.get('/members/info');
    return res.data;
  } catch (error) {
    console.error('Failed to load member info:', error);
    throw error;
  }
};

// 채팅방 리스트 로드
const loadChatRooms = async (memberId) => {
  try {
    const res = await axiosInstance.get(`/rooms/${memberId}`);
    return res.data;
  } catch (error) {
    console.error('Failed to load chat rooms:', error);
    throw error;
  }
};

export default {
  data() {
    return {
      memberId: null,
      chatRooms: []
    };
  },
  async created() {
    await this.fetchChatRooms();
  },
  computed: {
    isLogIn() {
      return !!this.$store.getters.getAccessToken;
    }
  },
  methods: {
    async fetchChatRooms() {
      try {
        const memberInfo = await loadMemberInfo();
        this.memberId = memberInfo.memberId;

        const chatRooms = await loadChatRooms(this.memberId);
        this.chatRooms = chatRooms;
      } catch (error) {
        console.error('Error fetching chat rooms:', error.response?.data || error);
      }
    },
    goToChatRoom(auctionId, memberId) {
      this.$router.push(`/chat/${auctionId}/${memberId}`);
    }
  }
};
</script>

<style scoped>
@import '@/assets/style.css';

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

strong {
  display: block;
  margin-top: 10px;
}

ul {
  padding-left: 20px;
}
</style>
