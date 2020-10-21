import randomString from 'random-string';
import { useEffect, useState } from 'react';
import { NativeModules } from 'react-native';
import { schedule } from '../Scheduler';

/**
 * @typedef {Object} Node
 * @property {String} id
 * @property {'node'} type
 * @property {{ x: Number, y: Number }} position
 * @property {float} rotation
 * @property {float} scale
 * @property {Node[]} children
 */

/**
 * @type {React.FunctionComponent<{
 *  renderScene: (float dt) => (Node[] | Node)
 * }>}
 */
const Renderer = ({
  renderScene,
}) => {
  const [id] = useState(randomString({ length: 32 }));

  useEffect(() => {
    if (renderScene) {
      const unregister = schedule(({ deltaTime }) => {
        const sceneGraph = renderScene(deltaTime);
  
        NativeModules.Cocos.registerRootCocosElement(id, {
          id,
          type: 'node',
          children: [].concat(sceneGraph),
          position: {
            x: 0,
            y: 0,
          },
        });
      });

      return () => {
        unregister();
        NativeModules.Cocos.unregisterRootCocosElement(id);
      };
    }
  }, [renderScene, id]);

  return null;
};

export default Renderer;
