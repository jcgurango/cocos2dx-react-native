import randomString from 'random-string';
import React, { useEffect, useState } from 'react';
import {
  StyleSheet,
  Text
} from 'react-native';
import Renderer from './cocos2d/graphics/Renderer';
import OverlayView from './cocos2d/OverlayView';
import { schedule } from './cocos2d/Scheduler';

const App = () => {
  const start = Date.now();

  useEffect(() => {
    const id = randomString({ length: 32 });

    return schedule(() => {
    });
  }, []);

  return (
    <>
      <OverlayView style={styles.container}>
        <Text style={styles.hello}>This text is rendered from React Native.</Text>
      </OverlayView>
      <Renderer
        renderScene={() => {
          const time = Date.now() - start;

          return [
            {
              id: 'test123',
              type: 'hello-world',
              position: {
                x: Math.cos(time / 16 / 180 * Math.PI) * 50 + 100,
                y: Math.sin(time / 16 / 180 * Math.PI) * 50 + 100,
              },
            },
            {
              id: 'test1234',
              type: 'hello-world',
              position: {
                x: Math.cos(time / 16 / 180 * Math.PI) * 75 + 150,
                y: Math.sin(time / 16 / 180 * Math.PI) * 75 + 100,
              },
              rotation: time / 16 * Math.PI,
            },
            {
              id: 'test12345',
              type: 'hello-world',
              position: {
                x: Math.cos(time / 16 / 180 * Math.PI) * 100 + 250,
                y: Math.sin(time / 16 / 180 * Math.PI) * 100 + 100,
              },
              scale: Math.sin(time / 1000) / 4 + 1,
            },
            {
              id: 'test0',
              type: 'hello-world',
              position: {
                x: 0,
                y: 0,
              },
            },
          ];
        }}
      />
    </>
  );
};

export default App;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center'
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
    color: 'white',
  }
});
