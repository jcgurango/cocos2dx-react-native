import React, { useEffect } from 'react';
import { NativeModules, View } from 'react-native';

/**
 * @type {React.FunctionComponent<import('react-native').ViewProps>}
 */
const OverlayView = ({
  children,
  ...props
}) => {
  useEffect(() => {
    NativeModules.Cocos.show();

    return () => {
      NativeModules.Cocos.hide();
    };
  }, []);

  return (
    <View {...props}>
      {children}
    </View>
  );
};

export default OverlayView;
