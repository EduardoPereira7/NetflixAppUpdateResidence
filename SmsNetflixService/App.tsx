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

  return (
    <View style={{padding: 20}}>
      <Text style={{fontSize: 20, marginBottom: 10}}>Logs:</Text>
      <ScrollView style={{maxHeight: 200}}>
        <Text>{logs || 'No logs available'}</Text>
      </ScrollView>
      <Button title="Clear Logs" onPress={clearLogs} />
    </View>
  );
};

export default App;
