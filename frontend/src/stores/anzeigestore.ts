import { reactive } from 'vue'
import { defineStore } from 'pinia'
import { Client, type IMessage } from '@stomp/stompjs'
import type { IAnzeigeDTD } from './IAnzeigeDTD'
import type { IFrontendNachrichtEvent } from './IFrontendNachrichtEvent'
import { useInfo } from '@/composables/useInfo' 

export const useAnzeigeStore = defineStore('anzeigestore', () => {
  const anzeigedata = reactive<{ ok: boolean; anzeigeliste: IAnzeigeDTD[] }>({
    ok: false,
    anzeigeliste: []
  })

  const { setzeInfo } = useInfo()

  // Nur eine STOMP-Client-Instanz für den ganzen Store
  let stompClient: Client | null = null

  async function updateAnzeigeListe(): Promise<void> {
    try {
      const response = await fetch('/api/anzeige')

      if (response.ok) {
        const daten: IAnzeigeDTD[] = await response.json()
        anzeigedata.anzeigeliste = daten
        anzeigedata.ok = true

        // Live-Updates nur nach erfolgreichem REST-Abruf starten
        startAnzeigeLiveUpdate()
      } else {
        anzeigedata.anzeigeliste = []
        anzeigedata.ok = false
        setzeInfo(response.statusText)
      }
    } catch (fehler) {
      // Backend nicht erreichbar (Netzwerkfehler, kein HTTP-Statuscode)
      console.log('Fehler beim Abruf von /api/anzeige:', fehler)
      anzeigedata.anzeigeliste = []
      anzeigedata.ok = false
      setzeInfo('Anzeigen konnten nicht vom Server geladen werden')
    }
  }

  function startAnzeigeLiveUpdate(): void {
    if (stompClient) {
      // schon verbunden bzw. Verbindungsaufbau läuft schon
      return
    }

    const wsurl = `ws://${window.location.host}/stompbroker`
    console.log('Baue STOMP-Verbindung auf:', wsurl)

    stompClient = new Client({ brokerURL: wsurl })

    stompClient.onWebSocketError = (event) => {
      console.log('STOMP WebSocket-Fehler:', event)
      setzeInfo('Verbindung für Live-Updates fehlgeschlagen')
    }

    stompClient.onStompError = (frame) => {
      console.log('STOMP-Protokollfehler:', frame)
      setzeInfo('Fehler bei den Live-Updates')
    }

    stompClient.onConnect = () => {
      console.log('STOMP verbunden, abonniere /topic/anzeige')

      stompClient!.subscribe('/topic/anzeige', (message: IMessage) => {
        const event: IFrontendNachrichtEvent = JSON.parse(message.body)
        console.log(JSON.stringify(event))

        if (event.typ === 'ANZEIGE') {
          updateAnzeigeListe()
        }
      })
    }

    stompClient.activate()
  }

  return {
    anzeigedata,
    updateAnzeigeListe
  }
})