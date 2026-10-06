export interface IFrontendNachrichtEvent {
  typ: 'ANZEIGE'
  id: number
  operation: 'CREATE' | 'UPDATE' | 'DELETE'
}