import { NativeEventEmitter, NativeModules } from 'react-native';
const eventEmitter = new NativeEventEmitter(NativeModules.Cocos);

/**
 * Schedules a callback to be called on every GL frame and returns a destructor.
 * 
 * @param {(e: { deltaTime: Number }) => void} callback 
 */
export const schedule = (callback) => {
  const listener = eventEmitter.addListener('cocosFrame', (params) => {
    callback(params);
  });

  return () => listener.remove();
};
