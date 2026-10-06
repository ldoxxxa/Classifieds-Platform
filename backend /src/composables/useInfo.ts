import { ref, readonly } from 'vue'

const info = ref<string>('Beachten Sie unsere aktuellen Anzeigen mit Sonderangeboten zur Ball-WM')

export function useInfo() {
  function loescheInfo(): void {
    info.value = ''
  }

  function setzeInfo(msg: string): void {
    info.value = msg
  }

  return {
    info: readonly(info),
    loescheInfo,
    setzeInfo
  }
}