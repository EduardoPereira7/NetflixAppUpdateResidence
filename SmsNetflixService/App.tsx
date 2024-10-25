import React, {useEffect, useState} from 'react';
import {
  Alert,
  Button,
  NativeModules,
  ScrollView,
  Text,
  View,
} from 'react-native';

const {LogModule} = NativeModules;

const App = () => {
  const [logs, setLogs] = useState<string>('');
  const [currentMessage, setCurrentMessage] = useState<string | null>(null);

  useEffect(() => {
    // Carrega o log de mensagens quando o componente é montado
    const loadLogs = async () => {
      try {
        const savedLogs = await LogModule.getLogs();
        setLogs(savedLogs);
      } catch (error) {
        console.error('Failed to load logs', error);
      }
    };

    loadLogs();
  }, []);

  // Função para limpar os logs
  const clearLogs = async () => {
    try {
      await LogModule.clearLogs();
      setLogs(''); // Limpa o estado local
      Alert.alert('Logs Cleared', 'The logs have been cleared.');
    } catch (error) {
      console.error('Failed to clear logs', error);
    }
  };

  // Função para substituir a mensagem por "Sem Mensagem"
  const removeMessage = async () => {
    try {
      const response = await fetch(
        'https://cxlnqtbmtvkzscowleil.supabase.co/rest/v1/message?id=eq.ca2dd11f-f6a3-4e05-b6b9-c40057b8724f',
        {
          method: 'PATCH',
          headers: {
            apikey:
              'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN4bG5xdGJtdHZrenNjb3dsZWlsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjkwOTk3MjcsImV4cCI6MjA0NDY3NTcyN30.qXE_dZ-Vp3EgE0Dvnf69U4Od7tvYmdkRjH_bbE86SxE',
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            content: 'Sem Mensagem',
          }),
        },
      );

      if (response.ok) {
        Alert.alert(
          'Message Removed',
          'The message has been replaced with "Sem Mensagem".',
        );
      } else {
        console.error('Failed to remove message', await response.text());
        Alert.alert('Error', 'Failed to remove the message.');
      }
    } catch (error) {
      console.error('Error removing message', error);
      Alert.alert('Error', 'An error occurred while removing the message.');
    }
  };

  // Função para buscar a mensagem atual na API
  const fetchMessage = async () => {
    try {
      const response = await fetch(
        'https://cxlnqtbmtvkzscowleil.supabase.co/rest/v1/message?id=eq.ca2dd11f-f6a3-4e05-b6b9-c40057b8724f',
        {
          method: 'GET',
          headers: {
            apikey:
              'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN4bG5xdGJtdHZrenNjb3dsZWlsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjkwOTk3MjcsImV4cCI6MjA0NDY3NTcyN30.qXE_dZ-Vp3EgE0Dvnf69U4Od7tvYmdkRjH_bbE86SxE',
            'Content-Type': 'application/json',
          },
        },
      );

      if (response.ok) {
        const data = await response.json();
        setCurrentMessage(data[0]?.content || 'Sem Mensagem');
      } else {
        console.error('Failed to fetch message', await response.text());
        Alert.alert('Error', 'Failed to fetch the message.');
      }
    } catch (error) {
      console.error('Error fetching message', error);
      Alert.alert('Error', 'An error occurred while fetching the message.');
    }
  };

  return (
    <View style={{padding: 20}}>
      <Text style={{fontSize: 20, marginBottom: 10}}>Logs:</Text>
      <ScrollView style={{maxHeight: 200}}>
        <Text>{logs || 'No logs available'}</Text>
      </ScrollView>
      <View style={{marginVertical: 10}} />
      <Button title="Clear Logs" onPress={clearLogs} />
      <View style={{marginVertical: 10}} />
      <Button title="Remove Message" onPress={removeMessage} />
      <View style={{marginVertical: 10}} />
      <Button title="Fetch Current Message" onPress={fetchMessage} />
      {currentMessage && (
        <Text style={{marginTop: 10, fontSize: 16}}>
          Current Message: {currentMessage}
        </Text>
      )}
    </View>
  );
};

export default App;
